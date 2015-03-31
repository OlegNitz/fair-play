package actors

import akka.actor.{Props, ActorRef, Actor}
import models.TradeMessage
import MessageProcessor._
import play.api.libs.json._

object UserActor {
  def props(out: ActorRef) = Props(new UserActor(out))

  case class TextMessage(msg: String)
}

class UserActor(out: ActorRef) extends Actor {
  import UserActor._

  messageProcessor ! UserStarted()

  def receive = {
    case msg: TradeMessage =>
      out ! Json.toJson(msg)
    case TextMessage(msg) =>
      out ! Json.toJson(msg)
    case msg: JsValue =>
      println(msg)
      out ! Json.toJson("I received your message: " + msg)
  }

  override def postStop() = {
    messageProcessor ! UserStopped()
  }
}
