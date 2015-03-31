package actors

import akka.actor.{Props, ActorRef, Actor}
import MessageProcessor._
import play.api.libs.json._

object UserActor {
  def props(out: ActorRef) = Props(new UserActor(out))

  case class TextMessage(msg: String)
}

class UserActor(out: ActorRef) extends Actor {

  messageProcessor ! UserStarted()

  def receive = {
    case TradingVolumeUpdate(tradingVolume) =>
      out ! Json.obj("type" -> "TradingVolume", "object" -> Json.toJson(tradingVolume))
  }

  override def postStop() = {
    messageProcessor ! UserStopped()
  }
}
