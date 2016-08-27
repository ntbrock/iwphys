require File.dirname(__FILE__) + '/../test_helper'
require 'user_controller'

# Raise errors beyond the default web-based presentation
class UserController; def rescue_action(e) raise e end; end

class UserControllerTest < Test::Unit::TestCase
  fixtures :users, :user_configurations

  NEO_PASSWORD = 'redpill'
  MORPHEUS_PASSWORD = 'nebekenezzer'
  TRINITY_LOGIN = 'trinity'
  TRINITY_NAME = 'Trinity'
  TRINITY_EMAIL = 'trinity@thematrix.com'
  TRINITY_PASSWORD = 'motorcycle'

  private 
  def login_morpheus
    post :login, :user=>{:login=>@morpheus.login, :password=>MORPHEUS_PASSWORD}
  end
  
  def set_user_configuration(email_confirmation, email_sender)
    c = UserConfiguration.get
    c.email_confirmation = email_confirmation
    c.email_sender = email_sender
    c.save!
  end
  
  def post_new_trinity
    post :new, :user=>{:login=>TRINITY_LOGIN, 
                       :name=>TRINITY_NAME, 
                       :email=>TRINITY_EMAIL, 
                       :password=>TRINITY_PASSWORD, 
                       :password_confirmation=>TRINITY_PASSWORD}
    trinity = User.find_by_login('trinity')
  end
  
  def post_new_trinity_errors
    post :new, :user=>{:login=>TRINITY_LOGIN,
                       :name=>TRINITY_NAME, 
                       :email=>TRINITY_EMAIL, 
                       :password=>TRINITY_PASSWORD, 
                       :password_confirmation=>'bad_confirmation'}
                       
  end
  
  def get_list_with_basic_authentication(format, password)
    encoded_login_password = Base64.encode64("#{@neo.login}:#{password}")
    @request.env[format]  = ["Basic #{encoded_login_password}"]
    get :list
  end
  
  def assert_trinity_created(trinity, user_count)
    assert_equal user_count + 1, User.count
    assert_equal TRINITY_LOGIN, trinity.login
    assert_equal TRINITY_EMAIL, trinity.email
    assert_equal TRINITY_NAME  , trinity.name
  end
  
  def assert_mail_sent(email_addy)
    assert_equal 1, @emails.size
    email = @emails[0] 
    assert_equal email_addy, email.to[0]
  end
  
  def assert_no_mail_sent
    assert_equal 0, @emails.size
  end
  
  def assert_not_logged_in
    assert_nil User.current
  end
  
  def assert_logged_in_as(user)
    assert_not_nil User.current
    current_user = User.current
    assert_equal user.login, current_user.login
  end
  
  def assert_is_admin(user)
    assert_equal 1, user.admin
  end
  
  def assert_is_not_admin(user)
    assert_equal 0, user.admin
  end
  
  def assert_is_activated(user)
    assert_equal 1, user.activated
  end
  
  def assert_is_not_activated(user)
    assert_equal 0, user.activated
  end
  
  def assert_successful_login(user)
    assert flash[:login_succeeded]
    assert_redirected_to :action=> :success
    assert_logged_in_as user
  end
  
  def assert_basic_authentication(format, password)
    get_list_with_basic_authentication format, password
    assert flash[:login_succeeded]
    assert_template 'list'
    assert_logged_in_as @neo    
  end

  public
  def setup
    @controller = UserController.new
    @request, @response = ActionController::TestRequest.new, ActionController::TestResponse.new
    @request.host = "localhost"
    @emails = ActionMailer::Base.deliveries 
    @emails.clear
  end  
  
  def test_GET_activate
    post :forgot_password, :user=>{:email=>@neo.email}
    neo = User.find(@neo.id)
    get :activate, :id=>@neo.id, :token=>neo.token  
    #COULDN'T TELL FROM CODE WHAT THIS DOES
  end
  
  def test_GET_forgot_password
    get :forgot_password
    assert_template 'forgot_password'
  end
  
  def test_POST_forgot_password
    post :forgot_password, :user=>{:email=>@neo.email}
    assert_mail_sent(@neo.email)
    assert_template 'forgot_password_done'
  end
  
  def test_POST_forgot_password_with_error
    post :forgot_password, :user=>{:email=>'bad email'}
    assert_no_mail_sent
    assert_equal "Can't find a user with email bad email.", flash[:notice]
    assert_template 'forgot_password'
  end
  
  def test_GET_new
    get :new
    assert_template 'new'
  end
  
  def test_POST_new_with_email_confirmation
    set_user_configuration(1, '')
    user_count = User.count
    trinity = post_new_trinity
    assert_trinity_created trinity, user_count
    assert_is_not_activated trinity
    assert_equal 'User created.', flash[:notice]
    assert_mail_sent trinity.email
    assert_not_logged_in
    assert_template 'created'
  end
  
  def test_POST_new_without_email_confirmation
    set_user_configuration(0, '')
    user_count = User.count
    trinity = post_new_trinity
    assert_trinity_created trinity, user_count
    assert_is_activated trinity
    assert_is_not_admin trinity
    assert_equal 'User created.', flash[:notice]
    assert_no_mail_sent
    assert_not_logged_in
    assert_template 'success'
  end
  
  def test_POST_new_with_promote_to_admin
    User.delete_all
    trinity = post_new_trinity
    assert_trinity_created trinity, 0
    assert_is_admin trinity
    assert_is_activated trinity
    assert_template 'admin_created'
    assert_no_mail_sent
    assert_logged_in_as trinity
  end
  
  def test_POST_new_with_errors
    user_count = User.count
    trinity = post_new_trinity_errors
    assert_equal 'Creation of new user failed.', flash[:notice]
    assert_tag :content => "Password doesn't match confirmation"
  end
  
  def test_GET_configure_without_admin_login
    get :configure
    assert_template nil
  end  
  
  def test_GET_configure_with_admin_login
    login_morpheus
    get :configure
    assert_template 'configure'
  end
  
  def test_GET_list_without_login
    get :list
    assert_template nil
  end
  
  def test_GET_list_with_login
    login_morpheus
    get :list
    assert_template 'list'
    assert_tag :content=> @neo.name
    assert_tag :content=> @morpheus.name
  end
  
  def test_POST_login
    post :login, :user=>{:login=>@neo.login, :password=>NEO_PASSWORD}
    assert_successful_login @neo
  end
  
  def test_POST_login_admin
    post :login_admin, :user=>{:login=>@morpheus.login, :password=>MORPHEUS_PASSWORD}
    assert_successful_login @morpheus
    assert_template nil
    #THIS CAN'T BE RIGHT, NEO IS NOT AN ADMIN, SHOULD REDIRECT?
    post :login_admin, :user=>{:login=>@neo.login, :password=>NEO_PASSWORD}
    assert_successful_login @neo
    assert_template nil
  end
  
  def test_POST_login_bad_password
    post :login, :user=>{:login=>@neo.login, :password=>'BAD_PASSWORD'}
    assert_equal false, flash[:login_succeeded]
    assert_not_logged_in
    assert_equal '401', @response.headers["Status"]
    assert_equal "Basic realm=\"localhost\"", @response.headers["WWW-Authenticate"]
    assert_response 401
  end
  
  def test_login_with_basic_auth_HTTP_AUTHORIZATION
    assert_basic_authentication 'HTTP_AUTHORIZATION', NEO_PASSWORD
  end

  def test_login_with_basic_auth_X_HTTP_AUTHORIZATION
    assert_basic_authentication 'X-HTTP_AUTHORIZATION', NEO_PASSWORD
  end
  
  def test_bad_login_with_basic_auth
    get_list_with_basic_authentication 'HTTP_AUTHORIZATION', 'BAD_PASSWORD'
    assert_nil flash[:login_succeeded]
    assert_template nil
    assert_not_logged_in
  end
  
  def POST_logout
    morpheus = User.find(@morpheus.id)
    login_morpheus
    assert_logged_in_as morpheus
    post :logout
    assert_not_logged_in
    assert flash[:skip_user_setup]
    assert_template :login 
  end
  
  def test_POST_configure_with_admin_login
    login_morpheus
    post :configure, :user_configuration=>{:email_confirmation=>'0', :email_sender=>'thearchitect@thematrix.com'}   
    assert_equal "Configuration saved.", flash[:notice]
    assert_equal 0, UserConfiguration.get.email_confirmation
    assert_equal 'thearchitect@thematrix.com', UserConfiguration.get.email_sender
  end
  
end
