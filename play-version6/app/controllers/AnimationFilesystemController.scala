package controllers

import java.net.URLDecoder
import java.util.UUID

import javax.inject._
import models.{Iwp6Animation, Iwp6FilesystemCollection, Iwp6PreviewCollection}
import org.mongodb.scala.model.Filters._
import play.api.{Configuration, Logger}
import play.api.libs.json.Json
import play.api.mvc._
import services.{IwpFilesystemBrowserService, IwpMongoClient, IwpServices}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AnimationFilesystemController @Inject()(cc: ControllerComponents,
                                              config: Configuration,
                                              services: IwpServices) extends IwpAbstractController(cc, services) {


  lazy val publicBaseUrl = config.get[String]("iwp.publicBaseUrl")
  lazy val designerBaseUrl = config.get[String]("iwp.designerBaseUrl")



  def browseCollections() = optAuthenticated { request =>

    Future {
      val topCollections = services.directoryBrowser.topCollections()

      Ok(views.html.animation.topCollections(request.user, topCollections))
    }
  }



  def browseCollection(collectionEncoded: String) = optAuthenticated { request =>

    Future {
      val collectionName = URLDecoder.decode(collectionEncoded, "UTF-8")

      // Logger.info(s"AnimationFilesystemController:26> collectionName: ${collectionName}")

      services.directoryBrowser.getCollection(collectionEncoded) match {

        case None => NotFound(s"No Such Collection Found:45 ${collectionEncoded}")

        case Some(collection) =>
          val dirs = services.directoryBrowser.findCollections(collection)
          val (success, failures) = services.directoryBrowser.findAnimationsWithFailures(collection)

          Ok(views.html.animation.browseCollection(request.user, collection, dirs, success, failures))
      }
    }

  }


  def getAnimation(collectionEncoded: String, filename: String) = optAuthenticated { request =>

    Future {
      services.directoryBrowser.getCollection(collectionEncoded) match {

        case None => NotFound(s"No Such Collection Found:61 ${collectionEncoded}")

        case Some(collection) => {

          services.directoryBrowser.getAnimation(collection, filename) match {
            case Failure(x) =>
              Logger.error(s"AnimationFilesystemController:38> Failure: ${x}")

              NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
            case Success(s) =>

              // 2019Nov14 Calculate designer link with JSON callback
              val callbackJsonUrl = java.net.URLEncoder.encode( publicBaseUrl + s"/animation/${collectionEncoded}/${filename}.json", "UTF-8")
              val designUrlO = request.user.map { user =>
                designerBaseUrl + s"?token=${user.token}&url=${callbackJsonUrl}"
              }

              Ok(views.html.animation.animation(request.user, collection, filename, s, preview = false, designerUrl = designUrlO ))
          }
        }
      }
    }
  }


  /** quick fix to support legacy Urls*/
  def getSubAnimation(collection: String, subCollection: String, filename: String) = optAuthenticated { request =>

    Future {
      val collectionEncoded = collection + "/" + subCollection

      services.directoryBrowser.getCollection(collectionEncoded) match {

        case None => NotFound(s"No Such Collection Found:61 ${collectionEncoded}")

        case Some(collection) => {

          services.directoryBrowser.getAnimation(collection, filename) match {
            case Failure(x) =>
              Logger.error(s"AnimationFilesystemController:38> Failure: ${x}")

              NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
            case Success(s) =>

              // 2019Nov14 Calculate designer link with JSON callback
              val callbackJsonUrl = java.net.URLEncoder.encode( publicBaseUrl + s"/animation/${collectionEncoded}/${filename}.json", "UTF-8")
              val designUrlO = request.user.map { user =>
                designerBaseUrl + s"?token=${user.token}&url=${callbackJsonUrl}"
              }


              Ok(views.html.animation.animation(request.user, collection, filename, s, preview = false, designUrlO))
          }
        }
      }
    }
  }




  def getAnimationJson(collectionEncoded: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    services.directoryBrowser.getCollection(collectionEncoded) match {

      case None => NotFound(s"No Such Collection Found: ${collectionEncoded}")

      case Some(collection) => {

        services.directoryBrowser.getAnimation(collection, filename) match {
          case Failure(x) =>
            Logger.error(s"AnimationFilesystemController:88> Failure: ${x}")

            NotFound(s"No valid animation: ${collection}/${filename}, Error: ${x}")
          case Success(s) => Ok(Json.prettyPrint(Iwp6Animation.toJson(s)))
        }
      }
    }
  }


  def getSubAnimationJson(collection: String, subCollection: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    val collectionEncoded = collection + "/" + subCollection

    services.directoryBrowser.getCollection(collectionEncoded) match {

      case None => NotFound(s"No Such Collection Found: ${collectionEncoded}")

      case Some(collection) => {

        services.directoryBrowser.getAnimation(collection, filename) match {
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

  def postAnimationPreview() = optAuthenticated { implicit request =>

    Future {
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

                  Iwp6Animation.fromJson(Some(animationFilename), Json.parse(animationJson)) match {

                    case Failure(x) =>
                      Logger.error(s"AnimationFileystemController:152> Unable to parse Animation Json: ${x}", x)
                      BadRequest(s"Unable to parse Animation Json: ${x}")
                    case Success(animation) =>

                      // Ok(s"animationJson: Iwp6Animation: ${animation}")

                      // SPECIAL - Never Authenticated on Preview so we can't clone.
                      Ok(views.html.animation.animation(None, collection, animationFilename, animation, preview = true))
                  }
              }

          }
      }
    }
  }


}
