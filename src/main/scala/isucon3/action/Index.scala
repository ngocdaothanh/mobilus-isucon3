package isucon3.action

import xitrum.annotation.GET
import isucon3.model.DB
import isucon3.view.IndexView
import xitrum.annotation.CachePageDay

@GET("")
@xitrum.annotation.CacheActionDay(1)
class Index extends DefaultLayout {
  def execute() {
    val total = db.countPublicMemos()
    val memos = db.getRecentPublicMemos(0)
    respondInlineView(IndexView.render(this, 0, total, memos))
  }
}
