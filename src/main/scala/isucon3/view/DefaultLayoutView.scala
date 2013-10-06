package isucon3.view

import scala.xml.Unparsed
import scalatags._
import xitrum.Action

import isucon3.action._
import isucon3.model.User
import isucon3.model.Session

object DefaultLayoutView {
  def render(action: Action, user: Option[User], session: Session) = Seq(
html(
  head(
    meta.attr("http-equiv" -> "Content-Type", "content" -> "text/html", "charset" -> "utf-8"),
    title("Isucon3"),
    link.attr("rel" -> "stylesheet", "href" -> action.publicUrl("css/bootstrap.min.css")),
    style("body{padding-top:60px}"),
    link.attr("rel" -> "stylesheet", "href" -> action.publicUrl("css/bootstrap-responsive.min.css")),
    link.attr("rel" -> "stylesheet", "href" -> action.url[Index])
  ),
  body(
    div.cls("navbar navbar-fixed-top")(
      div.cls("navbar-inner")(
        div.cls("container")(
          a.cls("btn btn-navbar").attr("data-toggle" -> "collapse", "data-target" -> ".nav-collapse")(
            span.cls("icon-bar"),
            span.cls("icon-bar"),
            span.cls("icon-bar")
          ),
          a.cls("btn btn-navbar").attr("href" -> "/")("Isucon3"),
          div.cls("nav-collapse")(
            ul.cls("nav")(
              li(
                a.attr("href" -> action.url[Index])("Home")
              ),
              if (user.isDefined) {
                Seq(
                  li(
                    a.attr("href" -> action.url[MyPage])("MyPage")
                  ),
                  li(
                    form.action(action.url[Signout]).attr("method" -> "post")(
                      input.attr("type" -> "hiden", "name" -> "sid", "value" -> session.token),
                      input.attr("type" -> "submit", "value" -> "SignOut")
                    )
                  )
                )
              } else {
                li(
                  a.attr("href" -> action.url[Signin])("SignIn")
                )
              }
            )
          )
        )
      )
    ),
    div.cls("container")(
      h2("Hello"+ (if (user.isDefined) user.get.username else "") + "!"),
      Unparsed(action.renderedView.toString)
    ),
    script.attr("type" -> "text/javascript", "src" -> action.publicUrl("js/jquery.min.js")),
    script.attr("type" -> "text/javascript", "src" -> action.publicUrl("js/bootstrap.min.js"))
  )
))
}
