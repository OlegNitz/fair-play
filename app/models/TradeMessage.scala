package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class TradeMessage(
  userId: String,
  currencyFrom: String, // "EUR", "USD", ...
  currencyTo: String,
  amountSell: Double,
  amountBuy: Double,
  rate: Double,
  timePlaced: String, //"24-JAN-15 10:27:44"
  originatingCountry: String // "FR"
)

object TradeMessage {
  implicit val toJson: Writes[TradeMessage] = (
    (JsPath \ "userId").write[String] and
    (JsPath \ "currencyFrom").write[String] and
    (JsPath \ "currencyTo").write[String] and
    (JsPath \ "amountSell").write[Double] and
    (JsPath \ "amountBuy").write[Double] and
    (JsPath \ "rate").write[Double] and
    (JsPath \ "timePlaced").write[String] and
    (JsPath \ "originatingCountry").write[String]
  )(unlift(TradeMessage.unapply))

  implicit val fromJson: Reads[TradeMessage] = (
    (JsPath \ "userId").read[String] and
    (JsPath \ "currencyFrom").read[String] and
    (JsPath \ "currencyTo").read[String] and
    (JsPath \ "amountSell").read[Double] and
    (JsPath \ "amountBuy").read[Double] and
    (JsPath \ "rate").read[Double] and
    (JsPath \ "timePlaced").read[String] and
    (JsPath \ "originatingCountry").read[String]
  )(TradeMessage.apply _)
}

