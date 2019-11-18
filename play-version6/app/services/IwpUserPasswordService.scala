package services

import java.util.UUID

import models.{Iwp6AuthenticationLog, Iwp6DesignerUser}
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters._

import scala.concurrent.{ExecutionContext, Future}

class IwpUserPasswordService(mongoClient: IwpMongoClient)(implicit ec: ExecutionContext) {

  def findByUsername(username: String): Future[Option[Iwp6DesignerUser]] = {
    mongoClient.designerUserCollection.find(
      Document("username" -> username )).headOption().map { h =>
      h.map { _.copy(password=None)}
    }
  }


  def findByUsernamePassword(login: String, password: String): Future[Option[Iwp6DesignerUser]] = {
/*    mongoClient.designerUserCollection.find(
      Document("username" -> username, "password" -> password )).headOption()
*/
    // New method that considers Username or Email
    mongoClient.designerUserCollection.find(
      and( equal("password", password ),
        or( equal("username", login), equal("email", login) ) )
    ).headOption()

  }


  def findByToken(token: UUID): Future[Option[Iwp6DesignerUser]] = {
    mongoClient.designerUserCollection.find( Document("token" -> token.toString )).headOption()
  }



  def logAuthentication(log: Iwp6AuthenticationLog): Future[Option[Boolean]] = {

    mongoClient.authenticationLogCollection().insertOne(log).toFuture().map { completed =>
      Some(true)
    }
  }

  def createDesignerUser(user: Iwp6DesignerUser) : Future[Option[Boolean]] = {

    // Prevent duplicate usernames
    mongoClient.designerUserCollection.find( Document("username" -> user.username ) ).headOption().flatMap { userO =>

      userO match {
        case Some(user) => throw new RuntimeException("Duplicate username found, preventing creation.")
        case None =>

          mongoClient.designerUserCollection().insertOne(user).toFuture().map { completed =>
            Some(true)
          }
      }
    }
  }


}
