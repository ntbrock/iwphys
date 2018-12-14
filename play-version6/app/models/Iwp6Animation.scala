package models

import play.api.libs.json._



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
                            value: String )


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


case class Iwp6Description( text: String )

case class Iwp6Objects( GraphWindow: Iwp6GraphWindow,
                        description: Iwp6Description,
                        input: Seq[Iwp6Input],
                        output: Seq[Iwp6Output],
                        solid: Seq[Iwp6Solid],
                        time: Iwp6Time,
                        window: Iwp6Window )

case class Iwp6Author(email: String,
                      name: String,
                      organization: String,
                      username: String )


case class Iwp6Animation(filename: Option[String],
                         author: Iwp6Author,
                         objects: Iwp6Objects ) {

}



object Iwp6Animation {

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


}

