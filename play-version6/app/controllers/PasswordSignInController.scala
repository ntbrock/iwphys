package controllers

import java.net.URLDecoder
import java.time.LocalDateTime

import javax.inject._
import models.{Iwp6Animation, Iwp6AuthenticationLog, Iwp6PreviewCollection}
import org.joda.time.DateTime
import play.api.{Configuration, Logger}
import play.api.libs.json.Json
import play.api.mvc._
import services.{IwpFilesystemBrowserService, IwpServices}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PasswordSignInController @Inject()(implicit ec: ExecutionContext,
                                         c: Configuration,
                                         cc: ControllerComponents,
                                         services: IwpServices ) extends IwpAbstractController(cc, services) {


  // For security, error messages session based.
  def signInPasswordForm() = Action { implicit request: Request[AnyContent] =>

    val errorMessage = request.session.get("errorMessage")

    // TODO, if authenticated, redirect

    Ok(views.html.signin.signInPasswordForm(errorMessage)).withNewSession

  }

  def signInPasswordPost() = Action.async { implicit request: Request[AnyContent] =>

    request.body.asFormUrlEncoded match {
      case None => Future.successful(BadRequest("Request Must be form Url Encoded"))
      case Some(form) =>

        form.get("username") match {
          case None => Future.successful(BadRequest("Form Missing 'username'"))
          case Some(usernameSeq) =>


            form.get("password") match {
              case None => Future.successful(BadRequest("Form Missing 'password'"))
              case Some(passwordSeq) =>

                val username = usernameSeq.mkString("")
                val password = passwordSeq.mkString("")



                services.userPassword.findByUsernamePassword(username, password) flatMap { userO =>


                  val log = Iwp6AuthenticationLog(username,
                    request.remoteAddress,
                    request.path,
                    request.headers.toSimpleMap,
                    LocalDateTime.now(),
                    userO.isDefined
                  )

                  // Access audit logging
                  services.userPassword.logAuthentication(log).map { logged =>

                    userO match {
                      case None =>

                        Redirect(routes.PasswordSignInController.signInPasswordForm())
                          .addingToSession("errorMessage" -> "Invalid Username Or Password")

                      case Some(user) =>

                        Ok("TODO Post Form: username: " + username + "   password: " + password + "  user: " + user)
                    }
                  }
                }
            }
        }
    }
  }



}
