package isucon3.action

import java.util.Random
import xitrum.annotation.{GET, POST, CacheActionDay}

import isucon3.model.Sha
import isucon3.view.SigninView

@GET("signin")
@CacheActionDay(1)
class Signin extends DefaultLayout {
  def execute() {
    respondInlineView(SigninView.render(this))
  }
}

object DoSignin {
  val random = new Random(System.currentTimeMillis())
}

@POST("signin")
class DoSignin extends DefaultLayout {
  def execute() {
    val username = param("username")
    val password = param("password")

    db.signin(username, password) match {
      case None =>
        respondInlineView(SigninView.render(this))

      case Some(user) =>
        session.clear()
        session("memoSession") = MemoSession(Some(user), Sha.hash256(DoSignin.random.nextLong().toString))
        redirectTo[MyPage]()
    }
  }
}
