package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.IwpFilesystemBrowserService

import scala.util.{Failure, Success}

/**
 * This controller is a demo for drag-and-drop using Dragula.
 */
@Singleton
class DesignerController @Inject()(cc: ControllerComponents,
                                iwpDirectoryBrowserService: IwpFilesystemBrowserService) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def demo2018() = Action { implicit request: Request[AnyContent] =>

    val popularName = "popular"

    iwpDirectoryBrowserService.getCollection(popularName) match {

      case None => Ok(views.html.designer.demo2018())

      case Some(popularCollection) => Ok(views.html.designer.demo2018())

    }
  }



  def getDesigner(collectionEncoded: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    iwpDirectoryBrowserService.getCollection(collectionEncoded) match {

      case None => NotFound(s"No Such Collection Found:61 ${collectionEncoded}")

      case Some(collection) => {

        iwpDirectoryBrowserService.getAnimation(collection, filename) match {
          case Failure(x) =>
            Logger.error(s"AnimationFilesystemController:38> Failure: ${x}")

            NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")

            // TODO Require authenticated user?

          case Success(s) => Ok(views.html.designer.designer(collection, filename, s, None))
        }
      }
    }
  }


}
