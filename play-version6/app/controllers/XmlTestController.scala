package controllers

import cats.syntax.all._
import com.lucidchart.open.xtract.XmlReader._
import com.lucidchart.open.xtract.{XmlReader, __}
import edu.ncssm.iwp.problemdb.DProblemXMLParser
import javax.inject._
import play.api.Logger
import play.api.mvc._


@Singleton
class XmlTestController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  // DProblem problem = DProblemXMLParser.load ( data.toString ( ) );


  def version4Read() = Action { implicit request: Request[AnyContent] =>

    // Read Xml
    val simpleXml = "<problem><author><username>username</username><name>name</name><organization>org</organization><email></email></author><objects><time>t1</time><window>w1</window><output>o1</output><output>o2</output><solid>s1</solid><output>o3</output></objects></problem>"

    val dproblem = DProblemXMLParser.load(simpleXml)

    Ok(s"Version4Read to DProblem: ${dproblem}")

  }


}
