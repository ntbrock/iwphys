package controllers

import java.io.FileReader

import com.lucidchart.open.xtract.XmlReader
import javax.inject._
import javax.script.{Invocable, ScriptEngineManager}
import play.api.Logger
import play.api.mvc._
import services.{IwpDifferenceCalculatorService, IwpMongoClient, IwpVersion4CalculatorService, IwpVersion6CalculatorService}


import com.lucidchart.open.xtract.{XmlReader, __}
import com.lucidchart.open.xtract.XmlReader._
import cats.syntax.all._


/**
  * https://blog.knoldus.com/parsing-xml-into-scala-case-classes-using-xtract/
  */

// Tasks -
//  0. Finish Testing - How to read A seq of strongly typed sub nodes.
//        IwpTimeTest, IwpWindowTest, IwpOutputTest, IwpSolidTest
//  1. Finish full Xml parser for Iwp objects to existing case classes
//  2. Add object sequencing to json case classes
//  3. Build a pure scala xml to json converter
//  4. Upate the animator to use the new json that has the ordered objects
//  5. Rip out all circular dependence stuff, and just depend on object ordering.


object XtractTest {
  implicit val reader: XmlReader[XtractTest] = (
    attribute[String]("hello"),
    attribute[String]("extra").optional,
    (__).read[String].optional
    ).mapN(apply _)
}

case class XtractTest( hello: String, extra: Option[String], inner: Option[String] )



object IwpAuthorTest {
  implicit val reader: XmlReader[IwpAuthorTest] = (
    (__ \ "username").read[String],
    (__ \ "name").read[String].optional,
    (__ \ "organization").read[String].optional,
    (__ \ "email").read[String].optional
    ).mapN(apply _)
}

case class IwpAuthorTest( username: String,
  name: Option[String],
  organization: Option[String],
  email: Option[String])



object IwpObjectTest {
  implicit val reader: XmlReader[IwpObjectTest] = (
    (__).read[String]
    ).map{ x =>
    println(s"XtractTestController:65> x: ${x}")
    IwpObjectTest(x)
  }
}

case class IwpObjectTest( guts: String )


object IwpObjectSequenceTest {
  implicit val reader: XmlReader[IwpObjectSequenceTest] = (
    (__).read(seq[IwpObjectTest])
    ).map(apply _)
}

case class IwpObjectSequenceTest( objects: Seq[IwpObjectTest] )


object IwpAnimationTest {
  implicit val reader: XmlReader[IwpAnimationTest] = (
    (__ \ "author").read[IwpAuthorTest],
    (__ \ "objects" ).read[IwpObjectSequenceTest]
  ).mapN(apply _)
}


case class IwpAnimationTest( author: IwpAuthorTest,
                             objects: IwpObjectSequenceTest )




@Singleton
class XtractTestController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {



  def xtractTest1_basic() = Action { implicit request: Request[AnyContent] =>

    val simpleXml = scala.xml.XML.loadString("<xtractTest hello=\"Taylor\" extra=\"Niall\"> 8/2/2019  </xtractTest>")

    val parsedTest = XmlReader.of[XtractTest].read(simpleXml)

    Ok(s"parsedTest: ${parsedTest}")

  }


  def xtractTest2_iwp() = Action { implicit request: Request[AnyContent] =>

    val simpleXml = scala.xml.XML.loadString("<problem><author><username>username</username><name>name</name><organization>org</organization><email></email></author><objects><time>t1</time><window>w1</window><output>o1</output><output>o2</output><solid>s1</solid><output>o3</output></objects></problem>")



    val objects = simpleXml \\ "problem" \\ "objects"

    objects.map { node =>

      node.child.map { child =>

        Logger.info(s"XtractTestController:120> ${child}  label: ${child.label}")

      }
    }


    // val parsedTest = XmlReader.of[IwpAnimationTest].read(simpleXml)

    Ok(s"iwpAnimation: ${simpleXml \\ "problem" \\ "objects" }")

  }




}
