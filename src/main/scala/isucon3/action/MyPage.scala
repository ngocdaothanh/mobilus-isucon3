package isucon3.action

import xitrum.annotation.GET

@GET("mypage")
class MyPage extends DefaultLayout {
  def execute() {
    respondView()
  }
}