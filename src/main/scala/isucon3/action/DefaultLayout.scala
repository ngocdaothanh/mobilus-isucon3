package isucon3.action

import xitrum.Action

import isucon3.model.User
import isucon3.view.DefaultLayoutView

case class MemoSession(user: Option[User], token: String) {
  // headers "Cache-Control" => "private"
}

trait DefaultLayout extends Action {
  protected lazy val memoSession = getCurrentSession()

  override def layout = DefaultLayoutView.render(this, memoSession).toXML

  private def getCurrentUser(): Option[User] = {
    null
  }

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
