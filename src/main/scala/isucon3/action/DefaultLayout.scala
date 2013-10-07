package isucon3.action

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import xitrum.{Action, Cache, SkipCsrfCheck}

import isucon3.model.User
import isucon3.view.DefaultLayoutView

case class MemoSession(user: Option[User], token: String) {
  // headers "Cache-Control" => "private"
}

trait DefaultLayout extends Action with SkipCsrfCheck {
  protected val db = isucon3.model.DBH2

  protected lazy val memoSession = getCurrentSession()

  // Only actions whose views don't use MemoSession can use action cache
  protected def removeActionCache() {
    Cache.removeAction(classOf[Index])
    Cache.removeAction(classOf[Recent])
    Cache.removeAction(classOf[ShowMemo])
    Cache.removeAction(classOf[Signin])
  }

  override def layout = DefaultLayoutView.render(this, memoSession).toXML

  private def getCurrentSession(): MemoSession = {
    session.get("memoSession") match {
      case Some(memoSession) =>
        response.setHeader("Cache-Control", "private")
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
