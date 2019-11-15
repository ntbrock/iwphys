package controllers

import javax.inject._
import models.{Iwp6Animation, Iwp6DesignerAnimation}
import play.api._
import play.api.libs.json.{JsBoolean, JsObject, Json}
import play.api.mvc._
import services.{IwpFilesystemBrowserService, IwpServices}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller is a demo for drag-and-drop using Dragula.
 */
@Singleton
class DesignerController @Inject()(c: Configuration,
                                   cc: ControllerComponents,
                                   services: IwpServices)(implicit ec: ExecutionContext) extends IwpAbstractController(cc, services) {



  def launchDesigner(filename: Option[String] ) = authenticated { request =>

    Future {
      val designerBaseUrl = c.get[String]("iwp.designerBaseUrl")
      // Spawn designer via redirect, passing the auth token

      filename match {
        case None =>
          Redirect(designerBaseUrl + s"?token=${request.user.token}")
        case Some(f) =>
          Redirect(designerBaseUrl + s"?token=${request.user.token}&filename=${java.net.URLEncoder.encode(f, "UTF-8")}")
      }

    }
  }


  def savePost(filename: String) = authenticated { request =>

    request.body.asJson match {
      case None => Future.successful(BadRequest( Json.obj("success" -> false, "message" -> "POST body musts be JSON")))

      case Some(bodyJson) =>

        // Does it parse?
        Iwp6Animation.fromJson(Some(filename), bodyJson) match {
          case Failure(x) =>
            Future.successful(
              BadRequest( Json.obj("success" -> false,
                "message" -> "Animation Json Does not Parse",
                "exception" -> x.getMessage )))

          case Success(animation) =>

            val designerAnimation = Iwp6DesignerAnimation( username = request.user.username,
              filename = filename,
              animationJson = Json.stringify(bodyJson) )

           services.designerAnimation.upsert( designerAnimation ).map { res =>

             Logger.info(s"DesignerController:56> Stored Animation: username: ${designerAnimation.username}  filename: ${designerAnimation.filename}  res: ${res}")
             Ok( Json.obj("success" -> true,
               "message" -> "Store Animation",
               "username" -> designerAnimation.username,
               "filename" -> designerAnimation.filename ) )

           }
        }
    }
  }



  def jsonGet(filename: String) = authenticated { request =>

    services.designerAnimation.findByUsernameFilename(request.user.username, filename) map { animationO =>

      animationO match {

        case Some(designerAnimation) =>

          Ok( designerAnimation.animationJson ).as("application/json")

        case None =>
          NotFound( Json.obj("success" -> false,
            "message" -> "Animation not found",
            "username" -> request.user.username,
            "filename" -> filename ) )

      }
    }
  }


}
