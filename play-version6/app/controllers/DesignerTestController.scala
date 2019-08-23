package controllers

import edu.ncssm.iwp.plugin.IWPObject
import edu.ncssm.iwp.problemdb.DProblemXMLParser
import javax.inject._
import models.Iwp6Animation
import play.api.mvc._
import services.IwpFilesystemBrowserService

import scala.util.{Failure, Success}


@Singleton
class DesignerTestController @Inject()(cc: ControllerComponents,
                                       iwpFilesystemBrowserService: IwpFilesystemBrowserService
                                 ) extends AbstractController(cc) {


  // DProblem problem = DProblemXMLParser.load ( data.toString ( ) );

  /**
    * Note this:
    * //---------------------------------------------------------------
    * //---------------------------------------------------------------
    * /**
    * IWP 3 - on initial zero of the problem, re-order the obejcts in the
    * problem based on their variable dependency.
    * 2007-Jan-29 brockman
    */
    * *
    * public synchronized Unit reorderProblemObjectsBySymbolicDependency ()
    * throws UnknownVariableException, CircularDependencyException, InvalidEquationException
    * {
    * @return
    */

  def version4ReadString() = Action { implicit request: Request[AnyContent] =>

    // Read Xml
    val simpleXml = "<problem><author><username>username</username><name>name</name><organization>org</organization><email></email></author><objects><time>t1</time><window>w1</window><output>o1</output><output>o2</output><solid>s1</solid><output>o3</output></objects></problem>"

    val dproblem = DProblemXMLParser.load(simpleXml)

    dproblem.objectsInDrawOrder

    Ok(s"version4ReadString to DProblem: ${dproblem}")

  }




  def version4CollectionRead(collection: String, filename: String) = Action { request =>


    iwpFilesystemBrowserService.getCollection(collection) match {
      case None => NotFound(s"No collection: ${collection}")
      case Some(collection) =>

        iwpFilesystemBrowserService.getVersion4Problem(collection, filename) match {

          case Failure(x) => BadRequest(s"Failure loading problem: ${x}")
          case Success(dproblem) =>


            val drawOrder = dproblem.getObjectsForDrawing.toArray.toSeq
              .map{ ar => ar.asInstanceOf[IWPObject] }
              .map{ o => s"name: ${o.getName} class: ${o.getClass.getName}" }
              .mkString("\n")

            val tickOrder = dproblem.getObjectsForTicking.toArray.toSeq.mkString("\n")


            Ok(s"version4DirectoryRead to DProblem:\n\n${dproblem}\n\nDrawOrder: ${drawOrder}\n\nTickOrder: ${tickOrder}")


        }


    }


  }


  def version4SubCollectionRead(collection: String, subCollection: String, filename: String) = Action { request =>


    iwpFilesystemBrowserService.getCollection(s"${collection}/${subCollection}") match {
      case None => NotFound(s"No collection: ${collection}")
      case Some(collection) =>

        iwpFilesystemBrowserService.getVersion4Problem(collection, filename) match {

          case Failure(x) => BadRequest(s"Failure loading problem: ${x}")
          case Success(dproblem) =>


            val drawOrder = dproblem.getObjectsForDrawing.toArray.toSeq
              .map{ ar => ar.asInstanceOf[IWPObject] }
              .map{ o => s"name: ${o.getName} class: ${o.getClass.getName}" }
              .mkString("\n")

            val tickOrder = dproblem.getObjectsForTicking.toArray.toSeq.mkString("\n")


            Ok(s"version4DirectoryRead to DProblem:\n\n${dproblem}\n\nDrawOrder: ${drawOrder}\n\nTickOrder: ${tickOrder}")

        }

    }

  }



  def version4SubCollectionConvert(collection: String, subCollection: String, filename: String) = Action { request =>

    iwpFilesystemBrowserService.getCollection(s"${collection}/${subCollection}") match {
      case None => NotFound(s"No collection: ${collection}")
      case Some(collection) =>

        iwpFilesystemBrowserService.getVersion4Problem(collection, filename) match {

          case Failure(x) => BadRequest(s"Failure loading problem: ${x}")
          case Success(dproblem) =>

            // Transform to a Iwp6Animation

            Iwp6Animation.fromDProblem(dproblem) match {
              case Failure(x) => BadRequest(s"Unable to Convert DProblem to Animation: ${x}")

              case Success(animation) => {
                val json = Iwp6Animation.toJson(animation)

                Ok(json).as("application/json")

              }
            }

            //Ok(s"version4SubCollectionConvert to Iwp6Animation: ${animation}");

        }


    }


  }


}
