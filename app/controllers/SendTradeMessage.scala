package controllers

import actors.MessageProcessor._
import models.TradeMessage
import play.api.mvc._
import play.api.libs.json._
import play.api.Logger

object SendTradeMessage extends Controller {

  def index = Action(BodyParsers.parse.tolerantJson) { request =>
    val placeResult = request.body.validate[TradeMessage]
    placeResult.fold(
      errors => {
        Logger.error(s"Error receiving TradeMessage: ${JsError.toFlatJson(errors)}")
        BadRequest(Json.obj("type" -> "Error", "text" -> JsError.toFlatJson(errors)))
      },
      msg => {
        messageProcessor ! MessageReceived(msg)
        Ok(Json.obj("type" -> "OK"))
      }
    )
  }
}
