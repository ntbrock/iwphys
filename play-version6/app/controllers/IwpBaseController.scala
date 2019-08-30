package controllers

import models.{Iwp6DesignerUser, IwpAuthenticatedRequest, IwpOptAuthenticatedRequest}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import org.slf4j.LoggerFactory
import pdi.jwt.JwtClaim
import play.api.Logger
import services.IwpEmailService

import scala.util.{Failure, Success}


/**
  * Action Composition For Sign In Authentication
  * @param cc
  * @param baseExecutionContext
  */

abstract class IwpBaseController(cc: ControllerComponents,
                                 iwpEmailSignInService: IwpEmailService)
                                 (private implicit val baseExecutionContext: ExecutionContext)
  extends AbstractController(cc) {

  val logger = new Logger(LoggerFactory.getLogger(this.getClass))




  private[controllers] def anonymous(f: Request[AnyContent] => Future[Result]) = Action.async { implicit request =>

    Logger.info("IwpBaseController:27> Anonymous")
    f(request)
  }

  protected def optAuthenticated(f: (IwpOptAuthenticatedRequest[AnyContent]) => Future[Result]) =
    optAuthenticatedT(parse.anyContent)(f)

  private[controllers] def authenticated(f: IwpAuthenticatedRequest[AnyContent] => Future[Result]) =
    authenticatedT(parse.anyContent)(f)


  private[controllers] def retrieveUserFromSession()(implicit request: Request[Any]) : Option[(Iwp6DesignerUser, Option[JwtClaim])] = {


    request.session.get("sign-in-email") match {

      case None =>
        None

      case Some(email) =>

        val claimO = request.session.get("sign-in-token") flatMap { token =>
          iwpEmailSignInService.decodeJwtClaim(token).toOption
        }

        Some(Iwp6DesignerUser(email, email), claimO)
    }
  }



  /**
    * Ensure Session is Proper
    *
    * @param bparser
    * @param f
    * @tparam T
    * @return
    */

  private[controllers] def authenticatedT[T](bparser: BodyParser[T])
                                            (f: IwpAuthenticatedRequest[T] => Future[Result]) = Action.async(bparser) { implicit request =>


    retrieveUserFromSession match {
      case None =>
        Future.successful(BadRequest("Not Authorized"))

      case Some(tuple) =>

        val (user,claimO) = tuple

        val authRequest = IwpAuthenticatedRequest[T](request, user, claimO)
        f(authRequest)
    }

  }



  protected def optAuthenticatedT[T](bparser: BodyParser[T])
                                    (f: (IwpOptAuthenticatedRequest[T]) => Future[Result]) = Action.async(bparser) { implicit request =>

    val authRequest = retrieveUserFromSession match {
      case None =>

        IwpOptAuthenticatedRequest[T](request, None, None)

      case Some(tuple) =>
        val (user, claimO) = tuple

        IwpOptAuthenticatedRequest[T](request, Some(user), claimO)
    }

    f(authRequest)
  }

}



