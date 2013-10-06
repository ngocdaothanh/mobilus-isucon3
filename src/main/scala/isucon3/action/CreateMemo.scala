package isucon3.action

import xitrum.Action
import xitrum.annotation.POST

import isucon3.model.DB

@POST("memo")
class CreateMemo extends DefaultLayout with RequireUser with AntiCsrf {
  def execute() {
    val content = param("content")
    val isPrivate =
      if (textParams.contains("is_private"))
        param[Int]("is_private").toInt == 1
      else
        false

    val memoId = DB.addMemo(memoSession.user.get, content, isPrivate)
    redirectTo[ShowMemo]("memoId" -> memoId)
  }
}
