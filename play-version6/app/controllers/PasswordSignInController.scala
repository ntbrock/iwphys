package controllers

import java.net.URLDecoder

import javax.inject._
import models.{Iwp6Animation, Iwp6PreviewCollection}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.IwpFilesystemBrowserService

import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PasswordSignInController @Inject()(cc: ControllerComponents,
                                         iwpDirectoryBrowserService: IwpFilesystemBrowserService ) extends AbstractController(cc) {


  def signInPasswordForm() = Action { implicit request: Request[AnyContent] =>


    val errorMessage = request.queryString.get("errorMessage").map { _.mkString("") }

    // TODO, if authenticated, redirect

    Ok(views.html.signin.signInPasswordForm(errorMessage))

  }

  def signInPasswordPost() = Action { implicit request: Request[AnyContent] =>

    request.body.asFormUrlEncoded match {
      case None => BadRequest("Request Must be form Url Encoded")
      case Some(form) =>

        form.get("username") match {
          case None => BadRequest("Form Missing 'username'")
          case Some(username) =>


            form.get("password") match {
              case None => BadRequest("Form Missing 'password'")
              case Some(password) =>

                Ok("TODO Post Form: username: " + username +  "   password: " + password )

            }


        }
    }




  }



}
