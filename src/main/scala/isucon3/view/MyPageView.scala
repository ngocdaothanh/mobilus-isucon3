package isucon3.view

import scalatags._
import xitrum.Action

import isucon3.action.{CreateMemo, ShowMemo, MemoSession}
import isucon3.model.Memo

object MyPageView {
  def render(action: Action, memoSession: MemoSession, memos: Seq[Memo]) = Seq(
form.action(action.absUrl[CreateMemo]).attr("method" -> "post")(
  input.attr("type" -> "hidden", "name" -> "sid", "value" -> memoSession.token),
  textarea.name("content"),
  br,
  input.attr("type" -> "checkbox", "name" -> "is_private", "value" -> "1"), "private",
  input.attr("type" -> "submit", "value" -> "post")
),

h3("my memos"),

ul(
  for (memo <- memos) yield
    li(
      a.href(action.absUrl[ShowMemo]("memoId" -> memo.id))(memo.title), memo.createdAt,
      if (memo.isPrivate) "[private]" else ""
    )
))
}
