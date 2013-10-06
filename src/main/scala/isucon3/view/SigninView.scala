package isucon3.view

import scalatags._
import xitrum.Action

import isucon3.action.DoSignin

object SigninView {
  def render(action: Action) = Seq(
form.action(action.url[DoSignin]).attr("method" -> "post")(
  "username", input.attr("type" -> "text", "name" -> "username", "size" -> "20"),
  br,
  "password", input.attr("type" -> "password", "name" -> "password", "size" -> "20"),
  br,
  input.attr("type" -> "submit", "value" -> "signin")
))
}
