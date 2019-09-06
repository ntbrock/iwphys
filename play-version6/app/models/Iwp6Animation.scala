package models

import java.io.File

import edu.ncssm.iwp.math.{MCalculator, MCalculator_Diff, MCalculator_Parametric}
import edu.ncssm.iwp.objects._
import edu.ncssm.iwp.problemdb.{DProblem, DProblemXMLParser}
import models.Iwp6Animation.fromXmlFile
import play.api.Logger
import play.api.libs.json._
import services.BoilerplateIO

import scala.util.{Failure, Success, Try}
import scala.io.Source



case class Iwp6Animation(filename: Option[String],
                         author: Iwp6Author,
                         objects: Seq[Iwp6Object] ) {

  def toJson : JsValue = Iwp6Animation.toJson(this)

  def toJsonString: String = Json.prettyPrint(toJson)

  def description: Option[Iwp6Description] = {
    objects.find(o => o.isInstanceOf[Iwp6Description]).map{_.asInstanceOf[Iwp6Description]}
  }
}



/**
  * Use this centralized parser!
  */

object Iwp6Animation extends BoilerplateIO {

  implicit val jsfIwp6Calculator = Json.format[Iwp6Calculator]
  implicit val jsfIwp6InitiallyOn = Json.format[Iwp6InitiallyOn]
  implicit val jsfIwp6GraphOptions = Json.format[Iwp6GraphOptions]
  implicit val jsfIwp6Path = Json.format[Iwp6Path]
  implicit val jsfIwp6Vectors = Json.format[Iwp6Vectors]
  implicit val jsfIwp6Length = Json.format[Iwp6Length]
  implicit val jsfIwp6Time = Json.format[Iwp6Time]
  implicit val jsfIwp6Window = Json.format[Iwp6Window]
  implicit val jsfIwp6Color = Json.format[Iwp6Color]
  implicit val jsfIwp6Shape = Json.format[Iwp6Shape]
  implicit val jsfIwp6Solid = Json.format[Iwp6Solid]
  implicit val jsfIwp6Output = Json.format[Iwp6Output]
  implicit val jsfIwp6Input = Json.format[Iwp6Input]
  implicit val jsfIwp6Description = Json.format[Iwp6Description]
  implicit val jsfIwp6GraphWindow = Json.format[Iwp6GraphWindow]
  implicit val jsfIwp6AuthorFormat = Json.format[Iwp6Author]

  // implicit val Iwp6Objects = Json.format[Seq[Iwp6Object]]
  //implicit val Iwp6AnimationFormat = Json.format[Iwp6Animation]


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


  def fromJsonFile(file: File) : Try[Iwp6Animation] = {

    Try {

      // Read file

      val jsonContents = Source.fromFile(file).getLines.mkString

      val jsv = Json.parse(jsonContents)

      fromJson(jsv) match {
        case Success(a) => a
        case Failure(x) => throw x
      }

    }

  }

  /**
    * Slightly Custom Inhertitance router
    */

  def fromJson(jsv: JsValue) : Try[Iwp6Animation] = {

    Try {

      Logger.info(s"Iwp6Animation:107> jsv: ${jsv}")


      throw new RuntimeException("Not Implemented")


    }

  }


  // Converter Helpers

  def convertCalc(mc: MCalculator): Iwp6Calculator = {
    if ( mc.isInstanceOf[MCalculator_Diff] ) { convertCalcDiff(mc.asInstanceOf[MCalculator_Diff]) }
    else if ( mc.isInstanceOf[MCalculator_Parametric] ) { convertCalcParam(mc.asInstanceOf[MCalculator_Parametric]) }
    else {
      throw new RuntimeException(s"Iwp6AnimationConverter:90> Unsupported MCalculator: ${mc.getClass.getName}")
    }
  }

  def convertCalcDiff(mc: MCalculator_Diff) : Iwp6Calculator = {
    Iwp6Calculator(
      calcType = mc.getType,
      displacement = Some(mc.getInitDispEqn.getEquationString),
      velocity = Some(mc.getInitVelEqn.getEquationString),
      acceleration = Some(mc.getAccelEqn.getEquationString),
      value = None
    )
  }


  def convertCalcParam(mc: MCalculator_Parametric) : Iwp6Calculator = {
    Iwp6Calculator(
      calcType = "parametric",
      displacement = None,
      velocity = None,
      acceleration = None,
      value = Some(mc.getEquationString)
    )
  }


  def fromFile(file: File) = {

    if ( file.getName.endsWith("iwp") ) {
      fromXmlFile(file)
    } else if ( file.getName.endsWith("js") ) {
      fromJsonFile(file)
    } else if ( file.getName.endsWith("json") ) {
      fromJsonFile(file)
    } else {
      throw new RuntimeException(s"Unknown File type by extension: ${file.getName}")
    }

  }



  def fromXmlFile(xmlFile: File) : Try[Iwp6Animation] = {

    val xmlString = readFileCompletely(xmlFile)

    Try {

      val problem = DProblemXMLParser.load(xmlString)

      problem.filename = xmlFile.getName
      fromDProblem(problem)

    }.flatten

    //  Try[Try[T]]  =>  .flatten Try[T]

  }


  def fromDProblem(dp: DProblem) : Try[Iwp6Animation] = {

    Try {

      val objects = dp.getObjectsForDrawing.toArray.toSeq.map { ar =>

        if ( ar.isInstanceOf[DObject_Description]) {
          val d = ar.asInstanceOf[DObject_Description]
          Some(Iwp6Description( text = Some(d.getText)))

        } else if ( ar.isInstanceOf[DObject_Time]) {
          val t = ar.asInstanceOf[DObject_Time]
          Some(Iwp6Time(
            start = t.getStartTime,
            stop = t.getStopTime,
            change = t.getChange,
            fps = t.getFps
          ))


        } else if ( ar.isInstanceOf[DObject_GraphWindow]) {
          val w = ar.asInstanceOf[DObject_GraphWindow]
          Some(Iwp6GraphWindow(
            xmin = w.getMinX,
            xmax = w.getMaxX,
            ymin = w.getMinY,
            ymax = w.getMaxY,
            xgrid = w.getGridX,
            ygrid = w.getGridY
          ))


        } else if ( ar.isInstanceOf[DObject_Window]) {
          val w = ar.asInstanceOf[DObject_Window]
          Some(Iwp6Window(
            xmin = w.getMinX,
            xmax = w.getMaxX,
            ymin = w.getMinY,
            ymax = w.getMaxY,
            xgrid = w.getGridX,
            ygrid = w.getGridY,
            xunit = Some(w.getUnitX),
            yunit = Some(w.getUnitY)
          ))


        } else if ( ar.isInstanceOf[DObject_Input]) {
          val i = ar.asInstanceOf[DObject_Input]
          Some(Iwp6Input(
            name = i.getName,
            text = Some(i.getText),
            initialValue = i.getInitialValue,
            units = Some(i.getUnits),
            ! i.isVisible
          ))

        } else if ( ar.isInstanceOf[DObject_Output]) {
          val o = ar.asInstanceOf[DObject_Output]
          Some(Iwp6Output(
            name = o.getName,
            text = Some(o.text),
            units = Some(o.getUnits),
            calculator = convertCalc(o.calc),
            ! o.isVisible
          ))

        } else if ( ar.isInstanceOf[DObject_Solid]) {
          val s = ar.asInstanceOf[DObject_Solid]
          Some(Iwp6Solid(
            name = s.name,

            shape = Iwp6Shape(
              s.shape.getType.toLowerCase,
              Some(Iwp6Vectors(
                xVel = s.shape.getVectorSelector.xVelSelected(),
                yVel = s.shape.getVectorSelector.yVelSelected(),
                xAccel = s.shape.getVectorSelector.xAccelSelected(),
                yAccel = s.shape.getVectorSelector.yAccelSelected(),
                Vel = s.shape.getVectorSelector.VelSelected(),
                Accel = s.shape.getVectorSelector.AccelSelected()
              )),
              Iwp6Length( convertCalc(s.shape.getWidthCalculator ) ),
              Iwp6Length( convertCalc(s.shape.getHeightCalculator ) ),
              Some(Iwp6GraphOptions(
                s.shape.getIsGraphable,
                Iwp6InitiallyOn(
                  s.shape.getGShape_GraphPropertySelector.xPosSelected(),
                  s.shape.getGShape_GraphPropertySelector.xVelSelected(),
                  s.shape.getGShape_GraphPropertySelector.xAccelSelected(),
                  s.shape.getGShape_GraphPropertySelector.yPosSelected(),
                  s.shape.getGShape_GraphPropertySelector.yVelSelected(),
                  s.shape.getGShape_GraphPropertySelector.yAccelSelected()
                )
              )),
              isGraphable = s.shape.getIsGraphable,
              drawTrails = s.shape.getIsDrawTrails,
              drawVectors = s.shape.getIsDrawVectors
            ),
            Iwp6Color(
              red = s.color.getRed,
              green = s.color.getGreen,
              blue = s.color.getBlue
            ),

            xpath = Iwp6Path( convertCalc(s.getCalcX )),
            ypath = Iwp6Path( convertCalc(s.getCalcY ))
          ))

        } else {
          //         val po = ar.asInstanceOf[Iwp6Object]


          Logger.error(s"Iwp6Animation:214> Unrecognized IwpV4 Dobject class: ${ar.getClass.getName }")
          None
        }

      }

      val author = Iwp6Author ( email = Some(dp.getAuthor.getEmail),
        name = Some(dp.getAuthor.getName),
        organization = Some(dp.getAuthor.getOrganization),
        username = Some(dp.getAuthor.getUsername)
      )

/*

case class Iwp6Animation(filename: Option[String],
                         author: Iwp6Author,
                         objects: Seq[Iwp6Object] ) {

 */
          Iwp6Animation( Some(dp.filename), author, objects.flatten )


    }
  }





  /*
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


  /**
    * 2019Mar01 A polymorphic json parsing approach
    */


  val obj = Json.fromJson[JsObject](json2)

  obj match {

    case e: JsError =>
      Logger.warn(s"IwpDirectoryBrowserService:56> Iwp Json Parse Error: ${file.getName} \n\n ${e})")
      throw new RuntimeException(s"Iwp Json Parse Error: ${file.getName}]\n${e})")

    case s: JsSuccess[Iwp6Animation] =>
      Success(s.value.copy(filename = Some(file.getName().replaceAll(".json$", "")) ))

  }
  */


  def toJson(animation: Iwp6Animation) : JsValue = {


    val objects = JsArray ( animation.objects.map { o =>

      if ( o.isInstanceOf[Iwp6Time] ) {
        Json.toJson(o.asInstanceOf[Iwp6Time])

      } else if ( o.isInstanceOf[Iwp6Description] ) {
        Json.toJson(o.asInstanceOf[Iwp6Description])

      } else if ( o.isInstanceOf[Iwp6Window] ) {
        Json.toJson(o.asInstanceOf[Iwp6Window])

      } else if ( o.isInstanceOf[Iwp6GraphWindow] ) {
        Json.toJson(o.asInstanceOf[Iwp6GraphWindow])

      } else if ( o.isInstanceOf[Iwp6Solid] ) {
        Json.toJson(o.asInstanceOf[Iwp6Solid])

      } else if ( o.isInstanceOf[Iwp6Input] ) {
        Json.toJson(o.asInstanceOf[Iwp6Input])

      } else if ( o.isInstanceOf[Iwp6Output] ) {
        Json.toJson(o.asInstanceOf[Iwp6Output])

      } else {
        throw new RuntimeException(s"Iwp6Animation:249> Unresolvable object type: ${o.getClass.getName}")
      }

    })


    JsObject(Map(
      "author" -> Json.toJson(animation.author),
      "objects" -> objects
    ))
  }


}

