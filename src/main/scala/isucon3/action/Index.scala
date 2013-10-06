package isucon3.action

import xitrum.annotation.GET

import isucon3.model.DB
import isucon3.view.IndexView

@GET("")
class Index extends DefaultLayout {
  def execute() {
    val total = DB.countPublicMemos()
    val memos = DB.getRecentPublicMemos()
    IndexView.render(this, 0, total, memos)

    respondView()
  }
}
