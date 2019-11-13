package models

import java.time.LocalDateTime

case class Iwp6AuthenticationLog(username: String,
                                 remoteAddress: String,
                                 path: String,
                                 headers: Map[String, String],
                                 createdOn: LocalDateTime,
                                 foundUser: Boolean )

