package isucon3.action

import xitrum.Action
import xitrum.annotation.POST

@POST("signout")
class Signout extends Action {
  def execute() {
    respondView()
  }
}
