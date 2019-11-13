package controllers

import javax.inject._
import models.{Iwp6Animation, Iwp6MongoCollection, Iwp6UserCollection}
import play.api.mvc._
import services.{IwpMongoClient, IwpServices}
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class DesignerAnimationBrowserController @Inject()(cc: ControllerComponents,
                                                   services: IwpServices) (implicit ec: ExecutionContext)
  extends IwpAbstractController(cc, services) {


  def browseUsername(username: String) = optAuthenticated { request =>


    services.userPassword.findByUsername(username) flatMap { userO =>

      userO match {

        case None => Future.successful(NotFound(s"No Such Username: ${username}"))

        case Some(user) =>

          services.designerAnimation.findByUsername(user.username).map { designerAnimations =>

            // Parse Animations, could be compute intensive

            val animations = designerAnimations.flatMap { da =>
              val jsv = Json.parse(da.animationJson)
              Iwp6Animation.fromJson( Some(da.filename), jsv ).toOption
            }

            // TODO show failed animations that don't parse, like the fileystem browser


            Ok(views.html.designerAnimation.browseUsername(request.user, user, animations))
          }
      }
    }
  }


  def getUserAnimation(username: String, filename: String)  = Action.async { implicit request: Request[AnyContent] =>


    services.designerAnimation.findByUsernameFilename(username, filename) map { animationO =>

      animationO match {
        case None =>
          NotFound(s"No Animation found for username: ${username}  with filename: ${filename}")

        case Some(animation) =>

          // Convert.
          val jsv = Json.parse(animation.animationJson)
          Iwp6Animation.fromJson(Some(animation.filename), jsv) match {
            case Failure(x) =>
              Logger.error("DesignerAnimationBrowser:69> ")
              NotFound(s"Animation found but failure parsing Json: ${x}")

            case Success(a) =>
              Ok(views.html.animation.animation(Iwp6UserCollection(username), filename, a))
          }


      }
    }

  }

}




/*
    mongo.animationCollection(collection).find().toFuture() map { animations =>

      Ok("TODO Refactor Mongo Browser")

      // Ok(views.html.animation.browseCollection(collection, Seq.empty,  animations))  // mongo has no subfolders
    }
*/




  /*
  def getAnimation(collection: String, filename: String) = Action.async { implicit request: Request[AnyContent] =>

    mongo.animationCollection(collection).find(equal("filename", filename)).toFuture() map { animations =>

      animations.headOption match {
        case None => NotFound(Json.obj("error"->true, "message"-> "Animation not found"))
        case Some(animation) =>

          Ok("TODO Refactor Mongo Browser")

          // Ok(views.html.animation.animation(collection, filename, animation))
      }
    }

  }



  def getAnimationJson(collection: String, filename: String) = Action.async { implicit request: Request[AnyContent] =>

    mongo.animationCollection(collection).find(equal("filename", filename)).toFuture() map { animations =>

      animations.headOption match {
        case None => NotFound(Json.obj("error"->true, "message"-> "Animation not found"))
        case Some(animation) =>
          Ok(Json.prettyPrint(Iwp6Animation.toJson(animation)))
      }
    }

  }




  def postAnimation(collection: String, filename: String) = Action.async { implicit request: Request[AnyContent] =>

    request.body.asJson match {
      case None => Future.successful(BadRequest(Json.obj("error" -> true, "message" -> "POST body was not json")))
      case Some(jsvPlay) =>

//        Future.successful(Ok(s"recieved file ${filename}"))

//        // Special Raw string conversions for the xtoj weirdness
//        val raw = Json.asciiStringify(jsvPlay)
//        val clean = raw
//          .replaceAll("@attributes", "attributes")
//          .replaceAll("\\{\\}", "\"\"")
//        val jsv = Json.parse(clean)

        val jsv = jsvPlay

        Logger.info(s"AnimationController:49> Received: $jsv")


        Iwp6Animation.fromJson(Some(filename), jsv) match {
          case Failure(x) =>
            Future.successful(BadRequest(s"Json schema error: ${x}"))
          case Success(iwpAnimation) =>

            // Write to DB

//            mongo.animationCollection(collection).insertOne(iwpAnimation.copy(filename=Some(filename))).toFuture() map { result =>

              Future.successful(Ok(s"Inserted Animation: $iwpAnimation"))
//            }

        }


    }


  }
*/
