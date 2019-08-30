package controllers


import java.util.UUID

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.{IwpEmailService, IwpFilesystemBrowserService}

import scala.util.{Failure, Success}

// https://www.playframework.com/documentation/2.7.x/ScalaForms
import play.api.data._
import play.api.data.Forms._


case class EmailSignInRequest ( email: String )


@Singleton
class EmailSignInController @Inject()(cc: ControllerComponents,
                                      iwpEmailService: IwpEmailService ) extends AbstractController(cc) {


  val emailSignInRequestForm = Form(
    mapping(
      "email" -> text
    )(EmailSignInRequest.apply)(EmailSignInRequest.unapply)
  )


  def signInEmailForm() = Action {

    Ok(views.html.signin.signInEmailForm())

  }


  def signInEmailSend() = Action {  implicit request: Request[AnyContent] =>

    emailSignInRequestForm.bindFromRequest.value match {

      case None =>
        BadRequest("Unable to parse form submission")

      case Some(form) =>

        val token = iwpEmailService.encodeJwtClaimToken(form.email)

        iwpEmailService.sendSignInEmail(form.email, token)

        Ok(s"Sent sign in: ${form.email} uuid: ${uuid}")

    }
    // READ post Body


  }


  def signInWithEmailToken(token: String) = Action { implicit request: Request[AnyContent] =>


    iwpEmailService.decodeJwtClaimToken(token) match {
      case Failure(x) =>
        Logger.error(s"Unable to decode email sign-in Token: ${x}", x)
        BadRequest(views.html.signin.signInEmailFailure(x.getMessage))

      case Success(jsObject) =>

        val email = (jsObject \ "email").as[String]

        Logger.info(s"Success with Sign In Email: ${email}  Token: ${jsObject}")

        // Look up email from token
        Ok( views.html.signin.signInEmailSuccess(email) ).withNewSession.addingToSession(
          "sign-in-email" -> email,
          "sign-in-token" -> token )

    }

  }


}

