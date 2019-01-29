package controllers

import java.net.URLDecoder

import javax.inject._
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.{IwpFilesystemBrowserService, IwpMongoClient}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AnimationFilesystemController @Inject()(cc: ControllerComponents,
                                              iwpDirectoryBrowserService: IwpFilesystemBrowserService ) extends AbstractController(cc) {




  def browseCollections() = Action { implicit request: Request[AnyContent] =>

    val topCollections = iwpDirectoryBrowserService.topCollections()

    Ok(views.html.animation.topCollections(topCollections))

  }



  def browseCollection(collectionEncoded: String) = Action { implicit request: Request[AnyContent] =>

    val collectionName = URLDecoder.decode(collectionEncoded, "UTF-8")

    // Logger.info(s"AnimationFilesystemController:26> collectionName: ${collectionName}")

    iwpDirectoryBrowserService.getCollection(collectionEncoded) match {

      case None => NotFound(s"No Such Collection Found:45 ${collectionEncoded}")

      case Some(collection) =>
        val dirs = iwpDirectoryBrowserService.findCollections(collection)
        val animations = iwpDirectoryBrowserService.findAnimations(collection)

        Ok(views.html.animation.browseCollection(collection, dirs, animations))
    }

  }


  def getAnimation(collectionEncoded: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    iwpDirectoryBrowserService.getCollection(collectionEncoded) match {

      case None => NotFound(s"No Such Collection Found:61 ${collectionEncoded}")

      case Some(collection) => {

        iwpDirectoryBrowserService.getAnimation(collection, filename) match {
          case Failure(x) =>
            Logger.error(s"AnimationFilesystemController:38> Failure: ${x}")

            NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
          case Success(s) => Ok(views.html.animation.animation(collection, filename, s))
        }
      }
    }
  }


  /** quick fix to support legacy Urls*/
  def getSubAnimation(collection: String, subCollection: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    val collectionEncoded = collection + "/" + subCollection

    iwpDirectoryBrowserService.getCollection(collectionEncoded) match {

      case None => NotFound(s"No Such Collection Found:61 ${collectionEncoded}")

      case Some(collection) => {

        iwpDirectoryBrowserService.getAnimation(collection, filename) match {
          case Failure(x) =>
            Logger.error(s"AnimationFilesystemController:38> Failure: ${x}")

            NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
          case Success(s) => Ok(views.html.animation.animation(collection, filename, s))
        }
      }
    }
  }




  def getAnimationJson(collectionEncoded: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    iwpDirectoryBrowserService.getCollection(collectionEncoded) match {

      case None => NotFound(s"No Such Collection Found: ${collectionEncoded}")

      case Some(collection) => {

        iwpDirectoryBrowserService.getAnimation(collection, filename) match {
          case Failure(x) =>
            Logger.error(s"AnimationFilesystemController:88> Failure: ${x}")

            NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
          case Success(s) => Ok(Json.prettyPrint(Json.toJson(s)))
        }
      }
    }
  }



}
