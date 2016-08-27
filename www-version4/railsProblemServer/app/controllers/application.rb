# The filters added to this controller will be run for all controllers in the application.
# Likewise will all the methods added be available for all controllers.
#
# Added Facilities for User model (and thus also for Modal and ModelSecurity).
#
require 'user_support'

class ApplicationController < ActionController::Base
  helper :ModelSecurity
  include UserSupport

  before_filter :user_setup

  # Pick a unique cookie name to distinguish our session data from others'
  session :session_key => '_railsProblemServer_session_id'
end

