package controllers

import java.io.File

import javax.inject._
import models.Iwp6Animation
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.{IwpDifferenceCalculatorService, IwpMongoClient, IwpVersion4CalculatorService, IwpVersion6CalculatorService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ValidationController @Inject()(cc: ControllerComponents,
                                    mongo: IwpMongoClient,
                                    iwpVersion4CalculatorService: IwpVersion4CalculatorService,
                                    iwpVersion6CalculatorService: IwpVersion6CalculatorService,
                                    iwpDifferenceCalculatorService: IwpDifferenceCalculatorService) extends AbstractController(cc) {


  def validateAnimation(collection: String, filename: String) = Action { implicit request: Request[AnyContent] =>

    val path = s"animations/${collection}/${filename}"

    val v4 = iwpVersion4CalculatorService.animateToJsonFrames(path)

    val v6 = iwpVersion6CalculatorService.animateToJsonFrames(s"${path}.json")

    val diffs = iwpDifferenceCalculatorService.diff(v4, v6)

    Ok(views.html.validation.compareIwpSteps(path, diffs))

  }


  def validateSubAnimation(collection: String, subCollection: String, filename: String) =
    validateAnimation(collection + File.separator + subCollection, filename )


}
