package actors

import actors.MessageProcessor.{TradingVolumeUpdate, Tick, MessageReceived}
import akka.actor._
import akka.testkit._
import models.{TradingVolume, TradeMessage}
import scala.concurrent.duration._

import org.specs2.mutable._
import org.specs2.time.NoTimeConversions

class TradingVolumeAccumulatorSpec extends TestkitExample with SpecificationLike with NoTimeConversions {

  sequential

  "A TradingVolumeAccumulator" should {
    "reply to Tick messages" in {
      within(500.millis) {
        val currencyPair = "EUR_USD"
        val amountBuyValue = 747.10
        val actor = TestActorRef(new TradingVolumeAccumulator(currencyPair))
        actor ! MessageReceived(TradeMessage(
          userId = "134256",
          currencyFrom = "EUR",
          currencyTo = "USD",
          amountSell = 1000.0,
          amountBuy = amountBuyValue,
          rate = 0.7471,
          timePlaced = "24-JAN-15 10:27:44",
          originatingCountry = "FR"
        ))
        actor ! Tick()
        expectMsg(TradingVolumeUpdate(TradingVolume(currencyPair, amountBuyValue)))
        ok("")
      }
    }
  }
}
