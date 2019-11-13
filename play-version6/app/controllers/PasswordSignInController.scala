package controllers

import java.net.URLDecoder
import java.time.{LocalDateTime, ZonedDateTime}
import java.util.UUID

import javax.inject._
import models.{Iwp6Animation, Iwp6AuthenticationLog, Iwp6DesignerUser, Iwp6PreviewCollection}
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
                    ZonedDateTime.now(),
                    userO.isDefined
                  )

                  // Access audit logging
                  services.userPassword.logAuthentication(log).map { logged =>

                    userO match {
                      case None =>

                        Redirect(routes.PasswordSignInController.signInPasswordForm())
                          .addingToSession("errorMessage" -> "Invalid Username Or Password")

                      case Some(user) =>

                        // Single Location to write auth session values
                        Ok(views.html.signin.signInPasswordSuccess(user))
                          .addingToSession("token" -> user.token.toString,
                            "authenticatedOn" -> LocalDateTime.now().toString)

                    }
                  }
                }
            }
        }
    }
  }


  // For security, error messages session based.
  def signInPasswordInitialize() = Action.async { implicit request: Request[AnyContent] =>
    // Ensure that the basic user exists.

    val initialUser = Iwp6DesignerUser( token = UUID.randomUUID().toString,
      email = "taylor.brockman@gmail.com",
      displayName = "Taylor Brockman",
      username = "brockman",
      password = Some("insecureInitializationPasswordDefaultljassdl1283923j32kj") )

    services.userPassword.createDesignerUser(initialUser).map { createdO =>
      Ok(s"createDesignerUser: ${createdO} initialUser: ${initialUser}")
    }


  }

}
