package isucon3.action

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import xitrum.annotation.GET

import isucon3.model.DB
import isucon3.view.ShowMemoView

@GET("memo/:memoId")
@xitrum.annotation.CacheActionDay(1)
class ShowMemo extends DefaultLayout {
  def execute() {
    val memoId = param[Int]("memoId")
    DB.getMemo(memoId) match {
      case None =>
        response.setStatus(HttpResponseStatus.NOT_FOUND)
        respondText("404 Not Found")

      case Some(memo) =>
        if (memo.isPrivate && (memoSession.user.isEmpty || memo.uid != memoSession.user.get.id)) {
          response.setStatus(HttpResponseStatus.NOT_FOUND)
          respondText("404 Not Found")
          return
        }

        val prev = DB.getPrevMemoId(memo.uid, memoId)
        val next = DB.getNextMemoId(memo.uid, memoId)
        respondInlineView(ShowMemoView.render(this, memo, prev, next))
    }
  }
}
