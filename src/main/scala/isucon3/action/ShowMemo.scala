package isucon3.action

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import xitrum.annotation.{GET, CacheActionDay}

import isucon3.view.ShowMemoView

@GET("memo/:memoId")
@xitrum.annotation.CacheActionDay(1)
class ShowMemo extends DefaultLayout {
  def execute() {
    val memoId = param[Int]("memoId")
    db.getMemo(memoId) match {
      case None =>
        response.setStatus(HttpResponseStatus.NOT_FOUND)
        respondText("404 Not Found")

      case Some(memo) =>
        if (memo.isPrivate && (memoSession.user.isEmpty || memo.uid != memoSession.user.get.id)) {
          response.setStatus(HttpResponseStatus.NOT_FOUND)
          respondText("404 Not Found")
          return
        }

        val prev = db.getPrevMemoId(memo.uid, memoId)
        val next = db.getNextMemoId(memo.uid, memoId)
        respondInlineView(ShowMemoView.render(this, memo, prev, next))
    }
  }
}
