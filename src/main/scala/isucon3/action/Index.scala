package isucon3.action

import xitrum.annotation.{GET, CacheActionDay}
import isucon3.view.IndexView

@GET("")
@CacheActionDay(1)
class Index extends DefaultLayout {
  def execute() {
    val total = db.countPublicMemos()
    val memos = db.getRecentPublicMemos(0)
    respondInlineView(IndexView.render(this, 0, total, memos))
  }
}
