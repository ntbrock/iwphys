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


object XtractTest {
  implicit val reader: XmlReader[XtractTest] = (
    attribute[String]("hello"),
    attribute[String]("extra").optional,
    (__).read[String].optional
    ).mapN(apply _)
}

case class XtractTest( hello: String, extra: Option[String], inner: Option[String] )



@Singleton
class XtractTestController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {



  def xtractTest1_basic() = Action { implicit request: Request[AnyContent] =>

    val simpleXml = scala.xml.XML.loadString("<xtractTest hello=\"Taylor\" extra=\"Niall\"> 8/2/2019  </xtractTest>")

    val parsedTest = XmlReader.of[XtractTest].read(simpleXml)

    Ok(s"simpleXml: ${parsedTest}")



  }


}
