package controllers

import java.net.URLDecoder

import javax.inject._
import models.{Iwp6Animation, Iwp6FilesystemCollection, Iwp6PreviewCollection}
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
        val (success, failures) = iwpDirectoryBrowserService.findAnimationsWithFailures(collection)

        Ok(views.html.animation.browseCollection(collection, dirs, success, failures))
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
          case Success(s) => Ok(Json.prettyPrint(Iwp6Animation.toJson(s)))
        }
      }
    }
  }

  /**
    * 2019Oct25 JSON post to Animation preview, the first designer hookin
    * @return
    */

  def postAnimationPreview() = Action { implicit request =>

    val collection = Iwp6PreviewCollection()

    request.body.asFormUrlEncoded match {
      case None => BadRequest("Missing formUrlEncoded")

      case Some(form) =>

        form.get("animationJson") match {
          case None => BadRequest("Form Missing animationJson key")
          case Some(animationJsonSeq) =>


            form.get("animationFilename") match {

              case None => BadRequest("Form Missing animationFilename key")
              case Some(animationFilenameSeq) =>

                val animationJson = animationJsonSeq.mkString("")
                val animationFilename = animationFilenameSeq.mkString("")

                Iwp6Animation.fromJson( Some(animationFilename), Json.parse(animationJson)) match {

                  case Failure(x) =>
                    Logger.error(s"AnimationFileystemController:152> Unable to parse Animation Json: ${x}", x)
                    BadRequest(s"Unable to parse Animation Json: ${x}")
                  case Success(animation) =>

                    // Ok(s"animationJson: Iwp6Animation: ${animation}")
                    Ok(views.html.animation.animation(collection, animationFilename, animation))
                }
            }

        }
    }
  }


}
