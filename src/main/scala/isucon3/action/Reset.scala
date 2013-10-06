package isucon3.action

import xitrum.Action
import xitrum.annotation.POST

@POST("reset")
class Reset extends Action {
  def execute() {
    respondView()
  }
}
