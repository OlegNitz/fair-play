package actors

import akka.actor.{ActorRef, Props, Actor}
import models.TradeMessage
import play.libs.Akka

import scala.collection.immutable.HashSet

object MessageProcessor {
  lazy val messageProcessor: ActorRef = Akka.system.actorOf(Props(classOf[MessageProcessor]))

  case class MessageReceived(msg: TradeMessage)
  case class UserStarted()
  case class UserStopped()
}

class MessageProcessor extends Actor {
  import MessageProcessor._
  import UserActor._

  protected[this] var users: HashSet[ActorRef] = HashSet.empty[ActorRef]
  
  def receive = {
    case MessageReceived(msg) =>
      users foreach (_ ! TextMessage(msg.toString))
    case UserStarted() =>
      users += sender
    case UserStopped() =>
      users -= sender
  }
}
