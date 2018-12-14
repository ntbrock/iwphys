package models

import java.io.File

import play.api.Logger
import play.api.libs.json._
import services.BoilerplateIO

import scala.util.{Success, Try}



case class Iwp6Time ( change: String,
                      fps: String,
                      start: String,
                      stop: String )


case class Iwp6Window ( xgrid: String,
                        xmax: String,
                        xmin: String,
                        xunit: String,
                        ygrid: String,
                        ymax: String,
                        ymin: String,
                        yunit: String )


case class Iwp6Calculator ( attributes : Map[String,String],
                            displacement: Option[String],
                            velocity: Option[String],
                            acceleration: Option[String],
                            value: Option[String] )


case class Iwp6Path ( calculator: Iwp6Calculator )

case class Iwp6Vectors ( attributes : Map[String,String] )

case class Iwp6Length ( calculator: Iwp6Calculator )


case class Iwp6InitiallyOn (attributes : Map[String,String] )

case class Iwp6GraphOptions (attributes : Map[String,String],
                             initiallyOn: Iwp6InitiallyOn )

case class Iwp6Shape (attributes : Map[String,String],
                      graphOptions: Iwp6GraphOptions,
                      height: Iwp6Length,
                      vectors: Iwp6Vectors,
                      width: Iwp6Length )



case class Iwp6Color ( blue: String,
                       green : String,
                       red: String )


case class Iwp6Solid ( color: Iwp6Color,
                        name: String,
                       shape: Iwp6Shape,
                       xpath: Iwp6Path,
                       ypath: Iwp6Path
                      )


case class Iwp6Output ( calculator: Option[Iwp6Calculator],
                        hidden: Option[String],
                       name: String,
                       text: String,
                       units: String )


case class Iwp6Input ( hidden: Option[String],
                        initialValue: String,
                        name: String,
                        text: String,
                        units: String )

case class Iwp6GraphWindow( xgrid: String,
                            xmax: String,
                            xmin: String,
                            ygrid: String,
                            ymax: String,
                            ymin: String )


case class Iwp6Description( text: Option[String] )

case class Iwp6Objects( GraphWindow: Iwp6GraphWindow,
                        description: Iwp6Description,
                        input: Seq[Iwp6Input],
                        output: Seq[Iwp6Output],
                        solid: Seq[Iwp6Solid],
                        time: Iwp6Time,
                        window: Iwp6Window )

case class Iwp6Author(email: Option[String],
                      name: Option[String],
                      organization: Option[String],
                      username: Option[String] )


case class Iwp6Animation(filename: Option[String],
                         author: Iwp6Author,
                         objects: Iwp6Objects ) {

}


/**
  * Use this centralized parser!
  */

object Iwp6Animation extends BoilerplateIO {

  implicit val Iwp6Calculator = Json.format[Iwp6Calculator]
  implicit val Iwp6InitiallyOn = Json.format[Iwp6InitiallyOn]
  implicit val Iwp6GraphOptions = Json.format[Iwp6GraphOptions]
  implicit val Iwp6Path = Json.format[Iwp6Path]
  implicit val Iwp6Vectors = Json.format[Iwp6Vectors]
  implicit val Iwp6Length = Json.format[Iwp6Length]
  implicit val Iwp6Time = Json.format[Iwp6Time]
  implicit val Iwp6Window = Json.format[Iwp6Window]
  implicit val Iwp6Color = Json.format[Iwp6Color]
  implicit val Iwp6Shape = Json.format[Iwp6Shape]
  implicit val Iwp6Solid = Json.format[Iwp6Solid]
  implicit val Iwp6Output = Json.format[Iwp6Output]
  implicit val Iwp6Input = Json.format[Iwp6Input]
  implicit val Iwp6Description = Json.format[Iwp6Description]
  implicit val Iwp6GraphWindow = Json.format[Iwp6GraphWindow]
  implicit val Iwp6Objects = Json.format[Iwp6Objects]
  implicit val Iwp6AuthorFormat = Json.format[Iwp6Author]
  implicit val Iwp6AnimationFormat = Json.format[Iwp6Animation]


  /**
    * Because of the weird php xml converter, single items are converted to objects
    * @param jsLookupResult
    * @return
    */
  private def toJsArray( jsLookupResult: JsLookupResult ) : JsArray = {

    jsLookupResult.toOption.map { i =>

      if ( i.isInstanceOf[JsArray] ) {
        i.asInstanceOf[JsArray]
      } else if ( i.isInstanceOf[JsObject] ) {
        JsArray(Seq(i))
      } else {
        throw new RuntimeException(s"Invalid javascript format for input: ${i}")
      }
    }.getOrElse(JsArray(Seq()))
  }


  def fromFile(file: File) : Try[Iwp6Animation] = {

    val jsonString = readFileCompletely(file)

    // Because of our xml -> json evolution , there's some cruft we manually fix
    val jsonMigrated1 = jsonString.replaceAll("@attributes", "attributes")
    val jsonMigrated2 = jsonMigrated1.replaceAll("[{][}]", "null")

    val json = Json.parse(jsonMigrated2)

    // Ensure top levels are good.

    val author = json \ "author"
    val objects = json \ "objects"
    val time = objects \ "time"
    val graphWindow = objects \ "GraphWindow"
    val window = objects \ "window"
    val description = objects \ "description"
    val input = toJsArray(objects \ "input")
    val solid = toJsArray(objects \ "solid")
    val output = toJsArray(objects \ "output")


    var json2 =
      Json.obj(
        "author" -> author.toOption,
        "objects" -> Json.obj(
          "time" -> time.toOption,
          "GraphWindow" -> graphWindow.toOption,
          "window" -> window.toOption,
          "description" -> description.toOption,
          "input" -> input,
          "solid" -> solid,
          "output" -> output
        )
      )


    val obj = Json.fromJson[Iwp6Animation](json2)

    obj match {

      case e: JsError =>
        Logger.warn(s"IwpDirectoryBrowserService:56> Iwp Json Parse Error: ${file.getName} \n\n ${e})")
        throw new RuntimeException(s"Iwp Json Parse Error: ${file.getName}]\n${e})")

      case s: JsSuccess[Iwp6Animation] =>
        Success(s.value.copy(filename = Some(file.getName().replaceAll(".json$", "")) ))

    }

  }
}

