package isucon3.action

import xitrum.annotation.POST

@POST("memo")
class CreateMemo extends DefaultLayout with RequireUser with AntiCsrf {
  def execute() {
    val content = param("content")
    val isPrivate =
      if (textParams.contains("is_private"))
        param[Int]("is_private").toInt == 1
      else
        false

    val memoId = db.addMemo(memoSession.user.get, content, isPrivate)
    removeActionCache()
    redirectTo[ShowMemo]("memoId" -> memoId)
  }
}
