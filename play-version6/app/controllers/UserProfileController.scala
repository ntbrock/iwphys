package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import services.{IwpEmailService, IwpFilesystemBrowserService, IwpServices}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserProfileController @Inject()(cc: ControllerComponents,
                                      services: IwpServices)(implicit ec: ExecutionContext) extends IwpAbstractController(cc, services) {


  def userProfile() = authenticated { request =>

    Future {
      Ok(views.html.userProfile.userProfile(request.user))
    }
  }

  def signOut() = Action {
    Redirect(routes.IndexController.index()).withNewSession
  }

}

