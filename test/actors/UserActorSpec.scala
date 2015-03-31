package actors

import actors.MessageProcessor.TradingVolumeUpdate
import akka.actor._
import akka.testkit._
import models.TradingVolume

import org.specs2.mutable._
import org.specs2.time.NoTimeConversions
import play.api.libs.json._

import play.api.test.WithApplication
import org.specs2.matcher.JsonMatchers

import scala.util.parsing.json.JSONObject

class UserActorSpec extends TestkitExample with SpecificationLike with JsonMatchers with NoTimeConversions {

  sequential

  "UserActor" should {

    val currencyPair = "EUR_USD"
    val volume = 123.0

    "send a TradingVolume message to a browser when receiving a TradingVolumeUpdate message" in new WithApplication {
      val out = TestActorRef[OutActor]

      val userActorRef = TestActorRef[UserActor](Props(new UserActor(out)))
      val userActor = userActorRef.underlyingActor

      userActor.receive(TradingVolumeUpdate(TradingVolume(currencyPair, volume)))

      val node = out.underlyingActor.actual.toString()
      node must /("type" -> "TradingVolume")
      node must /("object" -> JSONObject(Map("currencyPair" -> currencyPair, "volume" -> volume)))
    }
  }

}
