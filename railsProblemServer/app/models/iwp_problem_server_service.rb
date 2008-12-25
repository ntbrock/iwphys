# 2008-Jan-10 Brockman replacement, modern PS

class IWPProblemServerService < ActionWebService::Base
  web_service_api IWPProblemServerAPI

  def hello_world(word)
    return "Hello world: #{word}"
  end

#  api_method 'pps.authenticate', :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
#  api_method 'pps.listFiles',    :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
#  api_method 'pps.listDirectories', :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
#  api_method 'pps.getFile',      :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
 # api_method 'pps.putFile',      :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]
#  api_method 'pps.deleteFile',   :expects => [IWPProblemServerRequest], :returns => [IWPProblemServerResponse]

  def authenticate(req)
    r = IWPProblemServerResponse.new
    r.authkey = 'asdfasdfasdfasdfasfd'
    r
  end

  def listFiles(req)
    return IWPProblemServerResponse.new
  end

  def listDirectories(req)
    return IWPProblemServerResponse.new
  end

  def getFile(req)
    return IWPProblemServerResponse.new
  end

  def putFile(req)
    return IWPProblemServerResponse.new
  end

  def deleteFile(req)
    return IWPProblemServerResponse.new
  end

end

