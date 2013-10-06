package isucon3.action

import xitrum.Action
import xitrum.annotation.POST

import isucon3.model.DB

@POST("reset")
class Reset extends Action {
  def execute() {
    DB.reset()
    respondText("done")
  }
}
