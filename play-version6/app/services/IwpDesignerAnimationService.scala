package services

import java.time.ZonedDateTime
import java.util.UUID

import models.{Iwp6AuthenticationLog, Iwp6DesignerAnimation, Iwp6DesignerUser}
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.collection.immutable.Document
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

class IwpDesignerAnimationService(mongoClient: IwpMongoClient)(implicit ec: ExecutionContext) {


  def findByUsername(username: String): Future[Seq[Iwp6DesignerAnimation]] = {
    mongoClient.designerAnimationCollection.find(Document("username" -> username)).toFuture()
  }


  def findByUsernameFilename(username: String, filename: String): Future[Option[Iwp6DesignerAnimation]] = {
    mongoClient.designerAnimationCollection.find(
      Document("username" -> username, "filename" -> filename )).headOption()
  }

  def upsert(animation: Iwp6DesignerAnimation) : Future[Option[Boolean]] = {

    val f = mongoClient.designerAnimationCollection.find(
      Document("username" -> animation.username, "filename" -> animation.filename )).headOption.map { animationO =>

      animationO match {
        case None =>
          // insert
          Logger.info("IwpDesignerAnimationService:34> Inserting")
          mongoClient.designerAnimationCollection().insertOne(animation).toFuture().map { s =>
            Logger.info(s"IwpDesignerAnimationService:34> Insert Completed: ${s}")
            Some(true)
          }

        case Some(animation) =>
          // update
          Logger.info("IwpDesignerAnimationService:39> ReplaceOneing")

          mongoClient.designerAnimationCollection().replaceOne(
            Document("username" -> animation.username, "filename" -> animation.filename),
            animation.copy( updatedOn = Some(ZonedDateTime.now))).toFuture().map { s =>

            Logger.info(s"IwpDesignerAnimationService:34> ReplaceOne Completed: ${s}")
            Some(true)

          }

      }
    }

    f.flatten
  }


}
