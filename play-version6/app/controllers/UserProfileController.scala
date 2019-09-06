package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import services.{IwpEmailService, IwpFilesystemBrowserService}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserProfileController @Inject()(cc: ControllerComponents,
                                iwpDirectoryBrowserService: IwpFilesystemBrowserService,
                                iwpEmailService: IwpEmailService)(implicit ec: ExecutionContext) extends IwpBaseController(cc, iwpEmailService) {


  def userProfile() = authenticated { request =>

    Future {
      Ok(s"Hello ${request.user}")

    }
  }

  def signOut() = Action {
    Redirect(routes.IndexController.index()).withNewSession
  }

}

