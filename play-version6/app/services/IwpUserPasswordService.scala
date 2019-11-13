package services

import models.{Iwp6AuthenticationLog, Iwp6DesignerUser}
import org.mongodb.scala.bson.collection.immutable.Document

import scala.concurrent.{ExecutionContext, Future}

class IwpUserPasswordService(mongoClient: IwpMongoClient)(implicit ec: ExecutionContext) {


  def findByUsernamePassword(username: String, password: String): Future[Option[Iwp6DesignerUser]] = {
    mongoClient.designerUserCollection.find(
      Document("username" -> username, "password" -> password )).headOption()
  }


  def logAuthentication(log: Iwp6AuthenticationLog): Future[Option[Boolean]] = {

    mongoClient.authenticationLogCollection().insertOne(log).toFuture().map { completed =>
      Some(true)
    }
  }

}
