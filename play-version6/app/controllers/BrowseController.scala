package controllers

import javax.inject._
import play.api.mvc._
import services.IwpMongoClient
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BrowseController @Inject()(cc: ControllerComponents, mongo: IwpMongoClient) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def browse(collection: String) = Action.async { implicit request: Request[AnyContent] =>


    mongo.animationCollection(collection).find().toFuture() map { animations =>


      Ok(views.html.browse.browse(collection, animations))

    }

  }
}
