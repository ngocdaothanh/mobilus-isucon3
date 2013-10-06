package isucon3.view

import scalatags._
import xitrum.Action

import isucon3.action.ShowMemo
import isucon3.model.Memo

object ShowMemoView {
  def render(action: Action, memo: Memo, older: Option[Int], newer: Option[Int]) = Seq(
p.id("author")(
  if (memo.isPrivate) "Private" else "Public",
  "Memo by ", memo.username, memo.createdAt
),

hr,
if (older.isDefined)
  a.id("older").href(action.absUrl[ShowMemo]("memoId" -> older.get))("< older memo")
else
  ""
,
"|",
if (newer.isDefined)
  a.id("older").href(action.absUrl[ShowMemo]("memoId" -> newer.get))("newer memo >")
else
  ""
,

hr,
div.id("content_html")(memo.contentHtml)
  )
}
