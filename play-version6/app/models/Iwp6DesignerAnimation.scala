package models

import java.time.ZonedDateTime


// Mongo Case Class storage of designed animations

case class Iwp6DesignerAnimation ( username: String,
                                   filename: String,
                                   animationJson: String,
                                   createdOn: ZonedDateTime = ZonedDateTime.now,
                                   updatedOn: Option[ZonedDateTime] = None )
