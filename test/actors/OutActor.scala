package actors

import akka.actor.Actor
import play.api.libs.json.JsValue

class OutActor extends Actor {
  var actual: JsValue = _
  def receive = {
    case json: JsValue => actual = json
  }
}
