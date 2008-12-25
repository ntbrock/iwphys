# Manipulate the User model.
#
# This (and the User model) should probably be merged with ActiveRBAC.
#
# The HTTP authorization code is
# derived from an example published by Maximillian Dornseif at
# http://blogs.23.nu/c0re/stories/7409/
# which was released for use under any license.
#
# I started out with the Salted Hash login generator, and essentially rewrote
# the whole thing, learning a lot from the previous versions. This is not a
# criticism of the previous work, my goals were different. So, it's fair to
# say that this is derived from the work of Joe Hosteny and Tobias Leutke.
#
class UserController < ApplicationController
  layout 'default'
  scaffold :user
  helper :ModelSecurity

private
  # Require_admin will require an administrative login before the action
  # can be called. It uses Modal, so it will continue to the action if the 
  # login is successful.
  before_filter :require_admin, :only => [ :configure, :destroy ]

  # Require_login will require a login before the action
  # can be called. It uses Modal, so it will continue to the action if the 
  # login is successful.
  before_filter :require_login, :only => [ :list, :show ]

  # Cause HTTP Basic validation.
  # FIX: Basic is not secure. Change to Digest authentication.
  def http_authorize(realm=@request.domain(2))
    # This will cause the browser to send HTTP authorization information.
    @response.headers["Status"] = "Unauthorized" 
    @response.headers["WWW-Authenticate"] = "Basic realm=\"#{realm}\"" 
    render :status => 401
  end

  # Promote the first user to be an administrator, and don't make that user
  # confirm.
  def promote
    @user.activated = 1
    @user.admin = 1
    @user.save
  end

  # Send an email to a user with details on how to confirm his login.
  def send_confirmation_email(params)
    url_params = {
      :controller => 'user',
      :action => 'activate',
      :id => @user.id,
      :token => @user.new_token
    }
    url = url_for(url_params)
    UserMailer.deliver_new_user(params, url, @user.token_expiry)
  end

public

  before_filter :require_admin, :only => [ :configure, :destroy ]

  # Require_login will require a login before the action
  # can be called. It uses Modal, so it will continue to the action if the 
  # login is successful.
  before_filter :require_login, :only => [ :list, :show ]


  # Activate a new user, having logged in with a security token. All of the
  # work goes on in user_support.
  def activate
  end

  # Send out a forgot-password email.
  def forgot_password
    case @request.method
    when :get
      @user = User.new
    when :post
      @user = User.find_first(['email = ?', @params['user']['email']])
      if @user
        url = url_for(:controller => 'user', :action => 'activate', :id => @user.id, :token => @user.new_token)
        UserMailer.deliver_forgot_password(@user, url)
        render :action => 'forgot_password_done'
      else
        flash[:notice] = "Can't find a user with email #{@params['user']['email']}."
        @user = User.new
      end
    end
  end

  def list
    @users = User.find_all
  end

  # Attempt HTTP authentication, and fall back on a login form.
  # If this method is called login_admin (it's an alias), keep trying
  # until an administrator logs in or the user pushes the "back" button.
  def login
    if flash[:login_succeeded]
      redirect_back_or_default :action => :success
      return
    end

    @user = User.new
    
    flash[:login_succeeded] = false
    http_authorize
  end

  # Log in an administrator. If a non-administrator logs in, keep trying
  # until an administrator logs in or the user pushes the "back" button.
  alias login_admin login

  # Log out the current user, attempt HTTP authentication to log in a new
  # user. The session information skip_user_setup=true tells the server to
  # generate a new HTTP authentication request and ignore the current HTTP
  # authentication data.
  # We have to request new HTTP authentication here to make the browser
  # forget the old authentication data. Otherwise, the browser keeps sending
  # it!
  def logout
    User.sign_off
    reset_session
    flash[:skip_user_setup] = true
    redirect_to :action => 'login'
  end

  # Configure this system.
  def configure
    case @request.method
    when :get
      @user_configuration = UserConfiguration.get
    when :post
      c = (@user_configuration = UserConfiguration.get)
      c.attributes = @params['user_configuration']
      if c.save
         flash[:notice] = "Configuration saved."
      else
         flash[:notice] = "Configuration NOT saved."
      end
    end
  end
 
  # Create a new user.
  def new
    case @request.method
    when :get
      @user = User.new
    when :post
      p = @params['user']
      @user = User.new(p)

      if UserConfiguration.get.email_confirmation == 0
        @user.activated = 1
      end
      
      if @user.save
        flash[:notice] = 'User created.'
        # Promote the very first user to be an administrator.
        if @user.initial_self_promotion?
          promote
          User.current = @user
          render :action => 'admin_created'
        else
          # Mail the user instructions on how to activate their account.
          if UserConfiguration.get.email_confirmation == 1
            send_confirmation_email(p)
            @email = p['email']
            render :action => 'created'
          else
            render :action => 'success'
          end
        end
      else
        flash[:notice] = 'Creation of new user failed.'
      end
    end
  end

end
