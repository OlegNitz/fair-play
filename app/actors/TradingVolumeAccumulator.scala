package actors

import actors.MessageProcessor._
import akka.actor.{ActorLogging, Actor}
import models.TradingVolume

class TradingVolumeAccumulator(currencyPair: String) extends Actor with ActorLogging {
  var lastVolume: Double = 0.0
  var currentVolume: Double = 0.0

  def receive = {
    case MessageReceived(tradeMessage) =>
      currentVolume += tradeMessage.amountBuy
    case Tick() =>
      lastVolume = lastVolume * 0.99 + currentVolume
      currentVolume = 0.0
      sender ! TradingVolumeUpdate(TradingVolume(currencyPair, lastVolume))
  }
}
