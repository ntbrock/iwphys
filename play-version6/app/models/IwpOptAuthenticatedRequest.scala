package models

import pdi.jwt.JwtClaim
import play.api.mvc.{Request, WrappedRequest}


case class IwpOptAuthenticatedRequest[T](request: Request[T],
                                         user: Option[Iwp6DesignerUser],
                                      claim: Option[JwtClaim],
                                      ) extends WrappedRequest(request)



