package controllers

import javax.inject._
import models.Iwp6Animation
import play.api.mvc._
import services.IwpMongoClient
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AnimationMongoController @Inject()(cc: ControllerComponents, mongo: IwpMongoClient) extends AbstractController(cc) {


  def browseCollection(collection: String) = Action.async { implicit request: Request[AnyContent] =>

    mongo.animationCollection(collection).find().toFuture() map { animations =>

      Ok("TODO Refactor Mongo Browser")

      // Ok(views.html.animation.browseCollection(collection, Seq.empty,  animations))  // mongo has no subfolders
    }

  }


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
          Ok(Json.toJson(animation))
      }
    }

  }




  def postAnimation(collection: String, filename: String) = Action.async { implicit request: Request[AnyContent] =>

    request.body.asJson match {
      case None => Future.successful(BadRequest(Json.obj("error" -> true, "message" -> "POST body was not json")))
      case Some(jsvPlay) =>

        // Special Raw string conversions for the xtoj weirdness
        val raw = Json.asciiStringify(jsvPlay)
        val clean = raw
          .replaceAll("@attributes", "attributes")
          .replaceAll("\\{\\}", "\"\"")
        val jsv = Json.parse(clean)


        Logger.info(s"AnimationController:49> Received: $jsv")


        Json.fromJson[Iwp6Animation](jsv).asEither match {
          case Left(x) =>
            Future.successful(BadRequest(s"Json schema error: ${x}"))
          case Right(iwpAnimation) =>

            // Write to DB

            mongo.animationCollection(collection).insertOne(iwpAnimation.copy(filename=Some(filename))).toFuture() map { result =>

              Ok(s"Inserted Animation: $iwpAnimation")
            }

        }


    }


  }



}
