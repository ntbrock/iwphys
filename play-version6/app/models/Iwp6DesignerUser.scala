package models

import java.time.ZonedDateTime
import java.util.UUID

case class Iwp6DesignerUser ( token: String, // mongo compat
                              email: String,
                              displayName: String,
                              username: String,
                              password: Option[String] = None,
                              locationName: Option[String] = None,
                              schoolName: Option[String] = None,
                              personalBiography: Option[String] = None,
                              userAvatarUrl: Option[String] = None,
                              createdOn: ZonedDateTime = ZonedDateTime.now,
                              signInToken: Option[String] = None,
                              lastSignedInOn: Option[ZonedDateTime] = None ) {
  def tokenUuid: UUID = UUID.fromString(token)
}


