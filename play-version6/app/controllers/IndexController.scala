package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.{IwpServices}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class IndexController @Inject()(cc: ControllerComponents,
                                services: IwpServices)(implicit ec: ExecutionContext) extends IwpBaseController(cc, services) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = optAuthenticated { implicit request =>


    Future.successful {


      implicit val userO = request.user

      val popularName = "popular"

      services.directoryBrowser.getCollection(popularName) match {

        case None => Ok(views.html.index(None, Seq()))

        case Some(popularCollection) =>

          val (success,failures) = services.directoryBrowser.findAnimationsWithFailures(popularCollection)
          if ( failures.size > 0 ) {
            failures.map { f =>
              Logger.warn(s"Index:45> The popular collection had animation with failure: ${f.filename}: ${f.message}")

            }
          }
          Ok(views.html.index(Some(popularCollection), success))

      }
    }
  }

  def help(subpage: String = "") = optAuthenticated { implicit request =>

    Future.successful {
      if (subpage == "") {
        Ok(views.html.help(""))
      } else {
        Ok(views.html.help(subpage))
      }
      
    }
  }


}
