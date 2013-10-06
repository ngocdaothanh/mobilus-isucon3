package isucon3.action

import xitrum.annotation.GET

import isucon3.model.DB
import isucon3.view.MyPageView

@GET("mypage")
class MyPage extends DefaultLayout with RequireUser {
  def execute() {
    val memos = DB.getMyMemos(memoSession.user.get.id)
    respondInlineView(MyPageView.render(this, memos))
  }
}
