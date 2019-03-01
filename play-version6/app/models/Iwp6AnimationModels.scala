package models

import play.api.libs.json.{JsValue, Json}

/**
  * 2019Mar01 Flattening out the object type hierarchy to match V1 design
  * so that our animation library is forward compatible with teh version 6 designer
  */

trait Iwp6Object {
  def objectType: String
  def name: String
}

case class Iwp6Description( text: Option[String],
                            objectType: String = "description" ) extends Iwp6Object {

  def name = objectType

}

case class Iwp6Time ( start: Double,
                      stop: Double,
                      change: Double,
                      fps: Double,
                      objectType: String = "time" ) extends Iwp6Object {

  def name = objectType
}



case class Iwp6GraphWindow( xmin: Double,
                            xmax: Double,
                            ymin: Double,
                            ymax: Double,
                            xgrid: Double,
                            ygrid: Double,
                            objectType: String = "graphWindow") extends Iwp6Object {
  def name = objectType
}


case class Iwp6Window ( xmin: Double,
                        xmax: Double,
                        ymin: Double,
                        ymax: Double,
                        xgrid: Double,
                        ygrid: Double,
                        xunit: Option[String],
                        yunit: Option[String],
                        objectType: String = "window" ) extends Iwp6Object {

  def name = objectType

}



case class Iwp6Solid ( name: String,
                       shape: Iwp6Shape,
                       color: Iwp6Color,
                       xpath: Iwp6Path,
                       ypath: Iwp6Path,
                       objectType: String = "solid"
                     ) extends Iwp6Object


case class Iwp6Input ( name: String,
                       text: Option[String],
                       initialValue: Double,
                       units: Option[String],
                       hidden: Boolean = false,
                       objectType: String = "input" ) extends Iwp6Object

case class Iwp6Output ( name: String,
                        text: Option[String],
                        units: Option[String],
                        calculator: Iwp6Calculator,
                        hidden: Boolean = false,
                        objectType: String = "output" ) extends Iwp6Object


/**
  * Supporting Internal Objects
  * @param attributes
  * @param displacement
  * @param velocity
  * @param acceleration
  * @param value
  */


case class Iwp6Calculator ( calcType: String,
                            displacement: Option[String],
                            velocity: Option[String],
                            acceleration: Option[String],
                            value: Option[String] )


case class Iwp6Path ( calculator: Iwp6Calculator )

case class Iwp6Vectors ( xVel : Boolean,
                         yVel : Boolean,
                         xAccel: Boolean,
                         yAccel: Boolean,
                         Vel : Boolean,
                         Accel : Boolean )

case class Iwp6Length ( calculator: Iwp6Calculator )


case class Iwp6InitiallyOn (xPos : Boolean = false,
                            xVel : Boolean = false,
                            xAccel : Boolean = false,
                            yPos : Boolean = false,
                            yVel : Boolean = false,
                            yAccel : Boolean = false)

case class Iwp6GraphOptions ( graphVisible: Boolean,
                              initiallyOn: Iwp6InitiallyOn )

case class Iwp6Shape (shapeType: String,
                      vectors: Option[Iwp6Vectors],
                      width: Iwp6Length,
                      height: Iwp6Length,
                      graphOptions: Option[Iwp6GraphOptions],
                      isGraphable : Boolean = false,
                      drawTrails: Boolean = false,
                      drawVectors: Boolean = false )



case class Iwp6Color ( red: Int,
                       green : Int,
                       blue: Int )


case class Iwp6Author(email: Option[String],
                      name: Option[String],
                      organization: Option[String],
                      username: Option[String] )

