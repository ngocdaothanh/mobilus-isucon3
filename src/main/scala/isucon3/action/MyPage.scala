package isucon3.action

import xitrum.annotation.GET

import isucon3.model.DB
import isucon3.view.MyPageView

@GET("mypage")
@xitrum.annotation.CacheActionDay(1)
class MyPage extends DefaultLayout with RequireUser {
  def execute() {
    val memos = db.getMyMemos(memoSession.user.get.id)
    respondInlineView(MyPageView.render(this, memoSession, memos))
  }
}
