

# Send mail to a user to administer that user's login. Called by UserController.
class UserMailer < ActionMailer::Base
private
  def set_sender
    s = UserConfiguration.get.email_sender
    if s.length > 0
      from s
    end
  end

public
  # Send a forgot-password email, allowing the user to regain their login name
  # and password.
  def forgot_password(user, url)
    @body['user'] = user
    @body['url'] = url

    recipients	user.email
    subject	'Login and password recovery.'
    set_sender
  end

  # Send a new-user email, providing the user with a URL used to validate
  # that user's login.
  def new_user(params, url, token_expiry)
    @body['params'] = params
    @body['url'] = url
    @body['token_expiry'] = token_expiry

    recipients	params['email']
    subject	'Your new login is ready'
    set_sender
  end
end
