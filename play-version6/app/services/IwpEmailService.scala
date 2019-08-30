package services

import java.net.URLEncoder
import java.time.ZonedDateTime
import java.util.UUID

import javax.inject.Inject
import play.api.Configuration

import scala.concurrent.ExecutionContext
import org.apache.commons.mail._
import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.libs.json.{JsObject, Json}

import scala.util.Try


class IwpEmailService @Inject() (config: Configuration) (implicit ec: ExecutionContext) {

  val smtpEnabled = config.get[Boolean]("email.smtp.enabled")
  val smtpHost = config.get[String]("email.smtp.host")
  val smtpPort = config.get[Int]("email.smtp.port")
  val smtpUser = config.get[String]("email.smtp.user")
  val smtpPass = config.get[String]("email.smtp.pass")
  val smtpSender = config.get[String]("email.smtp.sender")
  val magicNumberBaseUrl = config.get[String]("iwp.publicBaseUrl")

  val jwtSecretKey = config.get[String]("jwt.secretKey")
  val jwtAlgo = JwtAlgorithm.HS256


  def claimBody(signInToken: String) =
    s"""
       |<h3>Interactive Web Physics - Sign In Link</h3>
       |
       |<p>Thank you for your interest in designing physics animations!</p>
       |
       |<p>To Sign In, <a href="${magicNumberBaseUrl}/sign-in/email-token?token=${signInToken}">please click here to sign in!</a>.</p>
       |
      |<p>Thank you!</p>
    """.stripMargin



  def encodeJwtClaimToken(email: String): String = {

    val jti = UUID.randomUUID().toString()
    val now = ZonedDateTime.now().toInstant.toEpochMilli
    val exp = ZonedDateTime.now().plusHours(4).toInstant.toEpochMilli

    val claim = Json.obj(("email", email), ("iat", now), ("jti", jti), ("exp", exp))


    val token = JwtJson.encode(claim, jwtSecretKey, jwtAlgo)

    token
  }

  def decodeJwtClaimToken(token: String) : Try[JsObject] = {

    JwtJson.decodeJson(token, jwtSecretKey, Seq(jwtAlgo))

  }

  def sendSignInEmail(address: String, token: String) {
    val subject = "Sign In Link - Interactive Web Physics"
    val urlEncodedCode = URLEncoder.encode(token.toString, "UTF-8")
    val body = claimBody(urlEncodedCode)
    sendEmail(address, subject, body)
  }


  def sendEmail(address: String, subject: String, body: String) {
    val email = new HtmlEmail()
    if (smtpEnabled) {
      email.setSubject(subject)
      email.setHtmlMsg(body)
      email.setFrom(smtpSender)
      email.addTo(address)
      email.setHostName(smtpHost)
      email.setSmtpPort(smtpPort)
      email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPass))
      email.setSSLOnConnect(true)
      email.send()
    }
  }
}
