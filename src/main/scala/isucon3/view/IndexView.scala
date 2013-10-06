package isucon3.view

import scalatags._
import xitrum.Action

import isucon3.action.ShowMemo
import isucon3.model.Memo

object IndexView {
  def render(action: Action, page: Int, total: Int, memos: Seq[Memo]): Seq[HtmlTag] = Seq(
h3("public memos"),
p.id("pager")(
  "recent ", (page * 100 + 1).toString, " - ", (page * 100 + 100).toString, "/", "total ", span.id("total")(total.toString)
),
ul.id("memos")(
  for (memo <- memos) yield
    li(
      a.href(action.absUrl[ShowMemo]("memoId" -> memo.id))(memo.title), " by ", memo.username, " (", memo.createdAt, ")"
    )
))
}
