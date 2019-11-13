package models

import java.time.{ZonedDateTime}

case class Iwp6AuthenticationLog(username: String,
                                 remoteAddress: String,
                                 path: String,
                                 headers: Map[String, String],
                                 createdOn: ZonedDateTime,
                                 foundUser: Boolean )

