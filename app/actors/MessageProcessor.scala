package actors

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import models.{TradingVolume, TradeMessage}
import play.libs.Akka
import scala.collection.immutable.HashSet
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object MessageProcessor {
  lazy val messageProcessor: ActorRef = Akka.system.actorOf(Props(classOf[MessageProcessor]))

  case class MessageReceived(msg: TradeMessage)
  case class UserStarted()
  case class UserStopped()
  case class Tick()
  case class TradingVolumeUpdate(tradingVolume: TradingVolume)
}

class MessageProcessor extends Actor with ActorLogging {
  import MessageProcessor._

  var users: HashSet[ActorRef] = HashSet.empty[ActorRef]

  context.system.scheduler.schedule(1.second, 1.second, self, Tick())
  
  def receive = {
    case msg @ MessageReceived(tradeMessage) =>
      val currencyPair = getCurrencyPair(tradeMessage)
      context.child(currencyPair).getOrElse {
        context.actorOf(Props(new TradingVolumeAccumulator(currencyPair)), currencyPair)
      } forward msg
    case UserStarted() =>
      users += sender
    case UserStopped() =>
      users -= sender
    case msg @ TradingVolumeUpdate(volume) =>
      users foreach (_ forward msg)
    case msg @ Tick() =>
      context.children foreach (_ ! msg)
  }

  def getCurrencyPair(tradeMessage: TradeMessage): String =
    s"${tradeMessage.currencyFrom}_${tradeMessage.currencyTo}"
}
