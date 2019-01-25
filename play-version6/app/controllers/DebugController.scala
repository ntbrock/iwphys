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
class DebugController @Inject()(cc: ControllerComponents, iwpDirectoryBrowserService: IwpFilesystemBrowserService) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>

   val topCollections = iwpDirectoryBrowserService.topCollections

    Ok(views.html.debug(topCollections))
  }
}
