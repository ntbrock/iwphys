package controllers

import java.io.File

import javax.inject._
import models.{Iwp6Animation, IwpObjectNameDiff, IwpObjectOrderingDiff, IwpOrderingRequiresProvides}
import org.mongodb.scala.model.Filters._
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import services.{IwpDifferenceCalculatorService, IwpMongoClient, IwpVersion4CalculatorService, IwpVersion6CalculatorService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ValidationController @Inject()(cc: ControllerComponents,
                                     configuration: Configuration,
                                    mongo: IwpMongoClient,
                                    iwpVersion4CalculatorService: IwpVersion4CalculatorService,
                                    iwpVersion6CalculatorService: IwpVersion6CalculatorService,
                                    iwpDifferenceCalculatorService: IwpDifferenceCalculatorService) extends AbstractController(cc) {


  val animationsPath = configuration.get[String]("iwp.animations.path")
  val sep = File.separator

  def validateAnimationCalculations(collection: String, filename: String, format: Option[String]) = Action { implicit request: Request[AnyContent] =>


    val path = s"${animationsPath}${sep}${collection}${sep}${filename}"

    val diffT = Try {

      val v4 = iwpVersion4CalculatorService.animateToJsonFrames(path)

      val v6 = iwpVersion6CalculatorService.animateToJsonFrames(collection, filename)

      val diffs = iwpDifferenceCalculatorService.diff(v4, v6)

      val differenceSummary = iwpDifferenceCalculatorService.summarize(path, diffs)



      (diffs, differenceSummary)
    }


    format match {
      case Some("csv") =>

        diffT match {
          case Success((diffs, differenceSummary)) =>
            Ok(differenceSummary.csvHeader.mkString(",")+"\n"+
              differenceSummary.csvValues.mkString(",")+"\n")

          case Failure(x) =>
          throw x
          //
          // Ok( Seq("\"Exception\"", "\""+path+"\"" , "\""+x.getMessage+"\"").mkString(",")+"\n" )

        }


      case None =>

        diffT match {
          case Success((diffs, differenceSummary)) =>
            Ok(views.html.validation.compareIwpSteps(path, diffs, differenceSummary))

          case Failure(x) => throw x
        }
    }
  }


  def validateSubAnimationCalculations(collection: String, subCollection: String, filename: String, format: Option[String]) =
    validateAnimationCalculations(collection + File.separator + subCollection, filename, format )



  def validateSubAnimationOrdering(collection: String, subCollection: String, filename: String, format: Option[String]) =
    validateAnimationOrdering(collection + File.separator + subCollection, filename, format )


  // 2019Sep06 A new dependency reordering calculator


  def validateAnimationOrdering(collection: String, filename: String, format: Option[String]) = Action { implicit request: Request[AnyContent] =>

    val path = s"${animationsPath}${sep}${collection}${sep}${filename}"


    // Ok("TODO: Implementing object ordering comparison")

    val v4array = iwpVersion4CalculatorService.problemObjectOrdering(path)

    val v6array = iwpVersion6CalculatorService.animateObjectOrdering(collection, filename)

    val v4ordering = v4array.value.zipWithIndex.map { case(jsv,i) =>
      val jso = jsv.asInstanceOf[JsObject]
      IwpOrderingRequiresProvides(
        order = i,
        name = (jso \ "name").as[String],
        provides = (jso \ "provided").asOpt[Seq[String]].getOrElse(Seq.empty).sorted.distinct,
        requires = (jso \ "required").asOpt[Seq[String]].getOrElse(Seq.empty).sorted.distinct,
        jso = jso
      )
    }

    val v6ordering = v6array.value.zipWithIndex.map { case(jsv,i) =>
      val jso = jsv.asInstanceOf[JsObject]
      IwpOrderingRequiresProvides(
        order = i,
        name = (jso \ "name").as[String],
        provides = (jso \ "provided").asOpt[Seq[String]].getOrElse(Seq.empty).sorted.distinct,
        requires = (jso \ "required").asOpt[Seq[String]].getOrElse(Seq.empty).sorted.distinct,
        jso = jso
      )
    }

    // Compile the case class by numerical difference

    val diffOrdering = v4ordering.zipWithIndex.map { case (v4order, i) =>
      val v6order = v6ordering(i)

      IwpObjectOrderingDiff(i,
        v4order,
        v6order,
        v4order.name.equals(v6order.name),
        v4order.requires.equals(v6order.requires),
        v4order.provides.equals(v6order.provides)
      )
    }

    // Compile the case class by object difference

    val diffNames = v4ordering.sortBy(_.name).map { v4order =>

      val v6order = v6ordering.find{_.name.equals(v4order.name)}.get

      IwpObjectNameDiff(v4order.name,
        v4order,
        v6order,
        v4order.order == v6order.order,
        v4order.requires.equals(v6order.requires),
        v4order.provides.equals(v6order.provides)
      )
    }


    Ok(views.html.validation.compareIwpOrdering(path, diffOrdering, diffNames) )

  }



    /*
    val diffT = Try {



      val v4 = iwpVersion4CalculatorService.animateToJsonFrames(path)

      val v6 = iwpVersion6CalculatorService.animateToJsonFrames(collection, filename)

      val diffs = iwpDifferenceCalculatorService.diff(v4, v6)

      val differenceSummary = iwpDifferenceCalculatorService.summarize(path, diffs)

      (diffs, differenceSummary)
    }


    format match {
      case Some("csv") =>

        diffT match {
          case Success((diffs, differenceSummary)) =>
            Ok(differenceSummary.csvHeader.mkString(",")+"\n"+
              differenceSummary.csvValues.mkString(",")+"\n")

          case Failure(x) =>
            throw x
          //
          // Ok( Seq("\"Exception\"", "\""+path+"\"" , "\""+x.getMessage+"\"").mkString(",")+"\n" )

        }


      case None =>

        diffT match {
          case Success((diffs, differenceSummary)) =>
            Ok(views.html.validation.compareIwpSteps(path, diffs, differenceSummary))

          case Failure(x) => throw x
        }
    }


*/

}
