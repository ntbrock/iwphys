package controllers

import java.net.URLDecoder

import javax.inject._
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.{IwpDirectoryBrowserService, IwpMongoClient}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AnimationFilesystemController @Inject()(cc: ControllerComponents,
                                              iwpDirectoryBrowserService: IwpDirectoryBrowserService ) extends AbstractController(cc) {



  def browseCollection(collectionEncoded: String) = Action { implicit request: Request[AnyContent] =>

    val collection = URLDecoder.decode(collectionEncoded, "UTF-8")

    Logger.info(s"AnimationFilesystemController:26> collection: ${collection}")

    val dirs = iwpDirectoryBrowserService.findFolders(collection)
    val animations = iwpDirectoryBrowserService.findAnimations(collection)

    Ok(views.html.animation.browseCollection(collection, dirs, animations))

  }



  def getAnimation(collection: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    iwpDirectoryBrowserService.getAnimation(collection, filename) match {
      case Failure(x) =>
        Logger.error(s"AnimationFilesystemController:38> Failure: ${x}")

        NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
      case Success(s) => Ok(views.html.animation.animation(collection,filename, s))
    }
  }

  def getAnimationJson(collection: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    iwpDirectoryBrowserService.getAnimation(collection, filename) match {
      case Failure(x) =>
        Logger.error(s"AnimationFilesystemController:48> Failure: ${x}")
        NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
      case Success(s) => Ok(Json.prettyPrint(Json.toJson(s)))
    }
  }


}
