package isucon3.action

import xitrum.annotation.GET

@GET("memo/:memoId")
class ShowMemo extends DefaultLayout {
  def execute() {
    respondView()
  }
}