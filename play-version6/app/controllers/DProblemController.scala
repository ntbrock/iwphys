package controllers

import java.time.LocalDateTime
import javax.inject._

import play.api._
import play.api.libs.json.{JsObject, JsString, Json}
import play.api.mvc._

@Singleton
class DProblemController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>


    val js = JsObject(Seq("hello"->JsString("world"), "now"-> JsString(LocalDateTime.now.toString)))


    Ok(Json.stringify(js)).as("application/json")
  }
}
