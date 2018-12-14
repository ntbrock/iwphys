package controllers

import javax.inject._
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.{IwpDirectoryBrowserService, IwpMongoClient}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AnimationFilesystemController @Inject()(cc: ControllerComponents,
                                              iwpDirectoryBrowserService: IwpDirectoryBrowserService ) extends AbstractController(cc) {



  def browseCollection(collection: String) = Action { implicit request: Request[AnyContent] =>

    val dirs = iwpDirectoryBrowserService.findFolders(collection)
    val animations = iwpDirectoryBrowserService.findAnimations(collection)

    Ok(views.html.animation.browseCollection(collection, dirs, animations))

  }


  def getAnimation(collection: String, filename: String) = Action.async { implicit request: Request[AnyContent] =>


    throw new RuntimeException("Not implemented")
  }

  def getAnimationJson(collection: String, filename: String) = Action.async { implicit request: Request[AnyContent] =>


    throw new RuntimeException("Not implemented")
  }




}
