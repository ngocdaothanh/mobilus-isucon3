package isucon3.action

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import xitrum.annotation.GET

import isucon3.model.DB
import isucon3.view.IndexView

@GET("recent/:page")
@xitrum.annotation.CacheActionDay(1)
class Recent extends DefaultLayout {
  def execute() {
    val page  = param[Int]("page")
    val total = db.countPublicMemos()
    val memos = db.getRecentPublicMemos(page)

    if (memos.isEmpty) {
      response.setStatus(HttpResponseStatus.NOT_FOUND)
      respondText("404 Not Found")
    } else {
      respondInlineView(IndexView.render(this, page, total, memos))
    }
  }
}
