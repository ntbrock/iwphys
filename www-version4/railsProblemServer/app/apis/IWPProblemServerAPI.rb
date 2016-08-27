# 2008-Jan-10 brockman
# Using Doc from : http://wiki.rubyonrails.org/rails/pages/HowtoWriteAXmlRpcService

require "IWPProblemServerRequest"
require "IWPProblemServerResponse"

class IWPProblemServerAPI < ActionWebService::API::Base
  inflect_names false

  # api_method :lookup_def, :expects => [:string], :returns => [:string]
  # api_method :define_word, :expects => [:string, :string], :returns => [:int]

  api_method 'hello_world',   :expects => [:string], :returns => [:string]

  api_method 'authenticate', :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
  api_method 'listFiles',    :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
  api_method 'listDirectories', :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
  api_method 'getFile',      :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
  api_method 'putFile',      :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
  api_method 'deleteFile',   :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]

end
