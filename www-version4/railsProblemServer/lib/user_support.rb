# UserSupport provides methods are intended to be included by
# ApplicationController to support the user system across the
# entire application. They are companions to the User model.
#
# The HTTP authorization code is
# derived from an example published by Maximillian Dornseif at
# http://blogs.23.nu/c0re/stories/7409/
# which was released for use under any license.
#
module UserSupport
end

require 'user'
require 'modal'

module UserSupport
  include Modal

  # Return true if the currently-logged-in user is the administrator.
  def admin?
    User.admin?
  end

  
  # This is meant to be used as a before_filter. 
  # A condition that is dependent on the user's login is in the block.
  # If the condition isn't true, a login panel is put up, and the explanation 
  # that is passed as an argument may (or may not) be presented to the user,
  # depending on whether we're using HTTP authentication or not.
  # Once the condition is met, it resumes the action it was protecting.
  def require_condition(header = nil, message = nil)
    if yield
      return true
    else
      if controller_name != 'user' or (action_name != 'login' and action_name != 'login_admin')
        store_location
      end

      # This test is to avoid writing the flash unnecessarily.
      # Currently, writing the flash causes the entire session, not just the
      # variables in question, to be written twice.
      if header or message
        flash[:login_header] = header
        flash[:login_message] = message
      end
      redirect_to :controller => 'user', :action => 'login'
      return false
    end
  end

  # This is meant to be used as a before_filter. It requires an
  # administrative login, putting up a login panel if the administrator
  # isn't currently logged in. Once the administrator logs in, it resumes
  # the action it was protecting.
  def require_admin
    header = "Administrative user required."
    message = "You must be an administrative user to perform this action. " \
     + "If you don't have an administrative login, please use the back button "\
     + "of your browser to cancel this action."
    require_condition(header, message) { admin? }
  end

  # This is meant to be used as a before_filter. It requires a
  # login, putting up a login panel if the session isn't currently
  # logged in. Once a user logs in, it resumes the action it was
  # protecting.
  def require_login
    require_condition(nil, "You must be logged in to perform this action.") { User.current }
  end

  # This is a before filter for the entire application, used to set up the
  # current user from the session or from various forms of authentication.
  # It's mandiatory that your application declare this filter if it's using
  # the User model, as this is responsible for maintaining the application's
  # idea of the currently-logged-in user.
  #
  # It will always return true, and thus will not block your actions. Use
  # require_login or require_admin if you want to block actions.
  #
  # This filter must be called before require_login, require_admin,
  # security tests of ModelSecurity that are based on User, or anything
  # that expects login information.
  #
  # Keep this function in sync with User.current() and User.current=().
  # It's aware of the way those functions store the user information.
  #
  
  def user_setup
    # require_* use Modal to return to what they were doing after HTTP
    # authentication.
    modal_setup

    # User.current=() needs a thread-global reference to the session.
    Thread.current[:session] = session
    logger.info("Session is #{Thread.current[:session]}")

    # This is used by the logout action to discard the old HTTP authentiction.
    # Logout redirects to login and that generates a new authentication
    # request. That request is the only input that can tell the browser to
    # stop sending the old authentication data with every request!
    if flash[:skip_user_setup] == true
      flash[:skip_user_setup] = false
      return true
    end

    login = password = nil
    r = @request.env
    old_user = user = session[:user]

    # If the request contains an HTTP authentication, decode it.
    # Don't use it to authenticate the user yet.
    if (authdata =  r['X-HTTP_AUTHORIZATION']) or (authdata = r['HTTP_AUTHORIZATION'])
      authdata = authdata.to_s.split

      # FIX: At the moment we only support Basic authentication. It's
      # prone to sniffing. Change to Digest authentication.
      if not authdata.nil? and authdata[0] == 'Basic' 
        login, password = Base64.decode64(authdata[1]).split(':')[0..1]
      end
    end

    # If the HTTP authentication is for a different user name, the user wants
    # to change logins. This can happen if an operation requires an
    # administrative login and the current user isn't the administrator but
    # also has an administrative login. It can also happen with command-line
    # tools like "wget" or web services clients that operate on behalf of
    # several users.
    #
    # Note that HTTP authentication (at least Basic) can be sent without
    # the server first asking for it, and that's valid according to the
    # HTTP specification. So, I can get here without having asked the browser
    # to put up a login panel.
    #
    # Allow re-log-in if the user name and password authenticate properly.
    #
    if login and (user.nil? or login != user.login)
      user = User.sign_on(login, password)
    end

    # Sign on the user via a web form.
    if @request.method == :post and (p = @params['user']) != nil
      if p['login'] and p['password']
        user = User.sign_on(p['login'], p['password'])
      end
    end

    # Sign on the user via a security token.
    if @params[:id] and @params['token']
      user = User.sign_on_by_token(@params[:id], @params['token'])
    end

    # User.current must always be set with each request. It's backed by a
    # class-global variable.
    User.current = user

    if user != old_user
      flash[:login_succeeded] = true
    end

    true
  end
end
