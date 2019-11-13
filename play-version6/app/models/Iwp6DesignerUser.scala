package models

import java.time.ZonedDateTime
import java.util.UUID

case class Iwp6DesignerUser ( token: UUID,
                              email: String,
                              displayName: String,
                              locationName: Option[String] = None,
                              schoolName: Option[String] = None,
                              personalBiography: Option[String] = None,
                              userAvatarUrl: Option[String] = None,
                              createdOn: ZonedDateTime = ZonedDateTime.now,
                              signInToken: Option[String] = None,
                              lastSignedInOn: Option[ZonedDateTime] = None )


