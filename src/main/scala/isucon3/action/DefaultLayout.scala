package isucon3.action

import xitrum.Action

import isucon3.model.User
import isucon3.view.DefaultLayoutView

case class MemoSession(user: Option[User], token: String) {
  // headers "Cache-Control" => "private"
}

trait DefaultLayout extends Action {
  protected lazy val memoSession = getCurrentSession()

  override def layout = DefaultLayoutView.render(this, memoSession)

  private def getCurrentUser(): Option[User] = {
    null
  }

  private def getCurrentSession(): MemoSession = {
    val memoSession = session("memoSession").asInstanceOf[MemoSession]
    if (memoSession != null) {
      memoSession
    } else {
      val ret = MemoSession(None, "")
      session("memoSession") = ret
      ret
    }
  }
}
