package isucon3.action

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import xitrum.{Action, SkipCsrfCheck}

import isucon3.model.User
import isucon3.view.DefaultLayoutView

case class MemoSession(user: Option[User], token: String) {
  // headers "Cache-Control" => "private"
}

trait DefaultLayout extends Action with SkipCsrfCheck {
  protected lazy val memoSession = getCurrentSession()

  override def layout = DefaultLayoutView.render(this, memoSession).toXML

  private def getCurrentSession(): MemoSession = {
    session.get("memoSession") match {
      case Some(memoSession) =>
        memoSession.asInstanceOf[MemoSession]

      case None =>
        val ret = MemoSession(None, "")
        session("memoSession") = ret
        ret
    }
  }
}

trait RequireUser {
  this: DefaultLayout =>

  beforeFilter {
    val ret = memoSession.user.isDefined
    if (!ret) redirectTo[Index]()
    ret
  }
}

trait AntiCsrf {
  this: DefaultLayout =>

  beforeFilter {
    val ret = bodyParams.contains("sid") && memoSession.token == param("sid")
    if (!ret) {
      response.setStatus(HttpResponseStatus.NOT_FOUND)
      respondText("400 Bad Request")
    }
    ret
  }
}
