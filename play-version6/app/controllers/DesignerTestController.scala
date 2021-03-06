package controllers

import java.time.ZonedDateTime
import java.util.UUID

import edu.ncssm.iwp.plugin.IWPObject
import edu.ncssm.iwp.problemdb.DProblemXMLParser
import javax.inject._
import models.{Iwp6Animation, Iwp6Author, Iwp6DesignerUser}
import play.api.libs.json.Json
import play.api.mvc._
import services.IwpFilesystemBrowserService

import scala.util.{Failure, Success}


@Singleton
class DesignerTestController @Inject()(cc: ControllerComponents,
                                       iwpFilesystemBrowserService: IwpFilesystemBrowserService
                                 ) extends AbstractController(cc) {

  implicit val jsfIwp6DesignerUserFormat = Json.format[Iwp6DesignerUser]

  def userSample() = Action { implicit request =>

    val taylor = Iwp6DesignerUser( token = "1141a76f-827d-4d2c-aa26-502a7aa40919",
      email = "taylor.brockman@gmail.com",
      displayName = "Taylor Brockman",
      username = "brockman",
      password = None,
      locationName = Some("Charleston, SC"),
      schoolName = Some("NCSSM"),
      personalBiography = Some("Class of 1999! Back in the day"),
      userAvatarUrl = None,
      createdOn = ZonedDateTime.now )

    Ok(Json.toJson(taylor))

  }


}
