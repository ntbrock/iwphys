package models

import play.api.libs.json._

case class IwpAnimation ( filename: String,
                          path: String ) {

}



object IwpAnimation {

  implicit val iwpAnimationFormat = Json.format[IwpAnimation]
}