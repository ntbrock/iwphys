# 2008-Jan-10 brockman. Using doc from: http://wiki.rubyonrails.org/rails/pages/HowtoWriteAXmlRpcService

class XmlrpcController < ApplicationController

 web_service_dispatching_mode :layered
 web_service :pps, IWPProblemServerService.new
  
end
