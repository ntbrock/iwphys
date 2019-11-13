package services

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.Configuration


/**
  * Iwp6Controller Injected Services
  * @param configuration
  * @param system
  */

class IwpServices @Inject()(configuration: Configuration,
                            system: ActorSystem){

  implicit val ec = system.dispatcher

  def email : IwpEmailService = new IwpEmailService(configuration)

  def directoryBrowser: IwpFilesystemBrowserService = new IwpFilesystemBrowserService(configuration)

  def user : IwpUserPasswordService = new IwpUserPasswordService()

}
