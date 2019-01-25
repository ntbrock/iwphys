package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.IwpFilesystemBrowserService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class IndexController @Inject()(cc: ControllerComponents,
                                iwpDirectoryBrowserService: IwpFilesystemBrowserService) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>

    val popularName = "popular"

    iwpDirectoryBrowserService.getCollection(popularName) match {

      case None => Ok(views.html.index(None, Seq()))

      case Some(popularCollection) => Ok(views.html.index(Some(popularCollection), iwpDirectoryBrowserService.findAnimations(popularCollection)))

    }
  }

}
