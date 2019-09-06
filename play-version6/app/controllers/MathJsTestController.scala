package controllers

import edu.ncssm.iwp.plugin.IWPObject
import edu.ncssm.iwp.problemdb.DProblemXMLParser
import javax.inject._
import models.Iwp6Animation
import play.api.mvc._
import services.IwpFilesystemBrowserService

import scala.util.{Failure, Success}


@Singleton
class MathJsTestController @Inject()(cc: ControllerComponents,
                                     iwpFilesystemBrowserService: IwpFilesystemBrowserService
                                 ) extends AbstractController(cc) {

  def stepFunction() = Action {

    Ok(views.html.test.mathjs.stepFunction())

  }

}
