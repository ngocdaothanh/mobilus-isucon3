package isucon3.action

import xitrum.annotation.POST

@POST("memo")
class CreateMemo extends DefaultLayout {
  def execute() {
    respondView()
  }
}
