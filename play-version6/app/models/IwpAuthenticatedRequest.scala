package models

import pdi.jwt.JwtClaim
import play.api.mvc.{Request, WrappedRequest}


case class IwpAuthenticatedRequest[T](request: Request[T],
                                      user: Iwp6DesignerUser,
                                      claim: Option[JwtClaim],
                                      ) extends WrappedRequest(request)


