package controllers

import actors.UserActor
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.Play.current

object Application extends Controller {

  def index = Action { request =>
    Ok(views.html.index.render(request))
  }

  def ws = WebSocket.acceptWithActor[JsValue, JsValue] { request => out =>
    UserActor.props(out)
  }

}