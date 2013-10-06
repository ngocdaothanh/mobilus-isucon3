package isucon3.action

import xitrum.annotation.POST

@POST("signout")
class Signout extends DefaultLayout {
  def execute() {
    respondView()
  }
}
