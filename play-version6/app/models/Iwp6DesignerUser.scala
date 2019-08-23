package models

import java.time.ZonedDateTime

case class Iwp6DesignerUser ( email: String,
                              displayName: String,
                              locationName: Option[String],
                              schoolName: Option[String],
                              personalBiography: Option[String],
                              userAvatarUrl: Option[String],
                              createdOn: ZonedDateTime,
                              signInToken: Option[String] = None,
                              lastSignedInOn: Option[ZonedDateTime] = None )


