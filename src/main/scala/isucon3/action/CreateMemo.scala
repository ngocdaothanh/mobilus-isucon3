package isucon3.action

import xitrum.Action
import xitrum.annotation.POST

@POST("memo")
class CreateMemo extends Action {
  def execute() {
    respondView()
  }
}
