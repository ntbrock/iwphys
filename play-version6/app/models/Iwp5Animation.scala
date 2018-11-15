package models

import play.api.libs.json._



case class Iwp5Time ( change: String,
                      fps: String,
                      start: String,
                      stop: String )


case class Iwp5Window ( xgrid: String,
                        xmax: String,
                        xmin: String,
                        xunit: String,
                        ygrid: String,
                        ymax: String,
                        ymin: String,
                        yunit: String )


case class Iwp5Calculator ( attributes : Map[String,String],
                            value: String )


case class Iwp5Path ( calculator: Iwp5Calculator )

case class Iwp5Vectors ( attributes : Map[String,String] )

case class Iwp5Length ( calculator: Iwp5Calculator )


case class Iwp5InitiallyOn (attributes : Map[String,String] )

case class Iwp5GraphOptions (attributes : Map[String,String],
                             initiallyOn: Iwp5InitiallyOn )

case class Iwp5Shape (attributes : Map[String,String],
                      graphOptions: Iwp5GraphOptions,
                      height: Iwp5Length,
                      vectors: Iwp5Vectors,
                      width: Iwp5Length )



case class Iwp5Color ( blue: String,
                       green : String,
                       red: String )


case class Iwp5Solid ( color: Iwp5Color,
                        name: String,
                       shape: Iwp5Shape,
                       xpath: Iwp5Path,
                       ypath: Iwp5Path
                      )


case class Iwp5Output ( calculator: Option[Iwp5Calculator],
                        hidden: Option[String],
                       name: String,
                       text: String,
                       // units: Option[String]  // ISSUE! Sometimes {}, Sometimes: ""
  )

case class Iwp5Input ( hidden: Option[String],
                        initialValue: String,
                        name: String,
                        text: String,
                        units: String )

case class Iwp5GraphWindow( xgrid: String,
                            xmax: String,
                            xmin: String,
                            ygrid: String,
                            ymax: String,
                            ymin: String )


case class Iwp5Description( text: String )

case class Iwp5Objects( GraphWindow: Iwp5GraphWindow,
                        description: Iwp5Description,
                        input: Seq[Iwp5Input],
                        output: Seq[Iwp5Output],
                        solid: Seq[Iwp5Solid],
                        time: Iwp5Time,
                        window: Iwp5Window )

case class Iwp5Author(email: Map[String,String],
                      name: Map[String,String],
                      organization: Map[String,String],
                      username: String )


case class Iwp5Animation(filename: Option[String],
                         author: Iwp5Author,
                         objects: Iwp5Objects ) {

}



object Iwp5Animation {

  implicit val iwp5Calculator = Json.format[Iwp5Calculator]
  implicit val iwp5InitiallyOn = Json.format[Iwp5InitiallyOn]
  implicit val iwp5GraphOptions = Json.format[Iwp5GraphOptions]
  implicit val iwp5Path = Json.format[Iwp5Path]
  implicit val iwp5Vectors = Json.format[Iwp5Vectors]
  implicit val iwp5Length = Json.format[Iwp5Length]
  implicit val iwp5Time = Json.format[Iwp5Time]
  implicit val iwp5Window = Json.format[Iwp5Window]
  implicit val iwp5Color = Json.format[Iwp5Color]
  implicit val iwp5Shape = Json.format[Iwp5Shape]
  implicit val iwp5Solid = Json.format[Iwp5Solid]
  implicit val iwp5Output = Json.format[Iwp5Output]
  implicit val iwp5Input = Json.format[Iwp5Input]
  implicit val iwp5Description = Json.format[Iwp5Description]
  implicit val iwp5GraphWindow = Json.format[Iwp5GraphWindow]
  implicit val iwp5Objects = Json.format[Iwp5Objects]
  implicit val iwp5AuthorFormat = Json.format[Iwp5Author]
  implicit val iwp5AnimationFormat = Json.format[Iwp5Animation]


}

