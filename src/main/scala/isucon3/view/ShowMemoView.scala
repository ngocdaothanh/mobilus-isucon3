package isucon3.view

import scala.xml.Unparsed
import scalatags._
import xitrum.Action

import isucon3.action.ShowMemo
import isucon3.model.Memo

object ShowMemoView {
  def render(action: Action, memo: Memo, prev: Option[Int], next: Option[Int]): Seq[HtmlTag] = Seq(
p.id("author")(
  if (memo.isPrivate) "Private" else "Public",
  "Memo by ", memo.username, memo.createdAt
),

hr,
if (prev.isDefined)
  a.id("older").href(action.absUrl[ShowMemo]("memoId" -> prev.get))("< older memo")
else
  span("")
,
span("|"),
if (next.isDefined)
  a.id("older").href(action.absUrl[ShowMemo]("memoId" -> next.get))("newer memo >")
else
  span("")
,

hr,
div.id("content_html")(Unparsed(memo.contentHtml))
  )
}
