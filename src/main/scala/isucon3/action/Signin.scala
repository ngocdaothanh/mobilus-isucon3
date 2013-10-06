package isucon3.action

import xitrum.annotation.{GET, POST}

@GET("signin")
class Signin extends DefaultLayout {
  def execute() {
    respondView()
  }
}

@POST("signin")
class DoSignin extends DefaultLayout {
  def execute() {
    respondView()
  }
}
