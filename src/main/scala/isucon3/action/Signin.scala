package isucon3.action

import java.util.Random
import xitrum.annotation.{GET, POST}

import isucon3.model.{DB, Sha}
import isucon3.view.SigninView

@GET("signin")
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

    DB.signin(username, password) match {
      case None =>
        respondInlineView(SigninView.render(this))

      case Some(user) =>
        session.clear()
        session("memoSession") = MemoSession(Some(user), Sha.hash256(DoSignin.random.nextLong().toString))
        redirectTo[MyPage]()
    }
  }
}
