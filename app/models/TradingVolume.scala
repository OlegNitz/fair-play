package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class TradingVolume(currencyPair: String, volume: Double)

object TradingVolume {
  implicit val toJson: Writes[TradingVolume] = (
    (JsPath \ "currencyPair").write[String] and
    (JsPath \ "volume").write[Double]
  )(unlift(TradingVolume.unapply))

  implicit val fromJson: Reads[TradingVolume] = (
    (JsPath \ "currencyPair").read[String] and
    (JsPath \ "volume").read[Double]
  )(TradingVolume.apply _)
}

