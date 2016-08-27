require File.dirname(__FILE__) + '/../test_helper'
require 'xmlrpc_controller'

# Re-raise errors caught by the controller.
class XmlrpcController; def rescue_action(e) raise e end; end

class XmlrpcControllerTest < Test::Unit::TestCase
  def setup
    @controller = XmlrpcController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new
  end

  # Replace this with your real tests.
  def test_truth
    assert true
  end
end
