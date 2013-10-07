package isucon3.model

trait DB {
  def reset(): Unit

  def signin(username: String, password: String): Option[User]

  def countPublicMemos(): Int

  def getRecentPublicMemos(page: Int): Seq[Memo]

  def getMyMemos(userId: Int): Seq[Memo]

  def addMemo(user: User, content: String, isPrivate: Boolean): Int

  def getMemo(memoId: Int): Option[Memo]

  def getPrevMemoId(uid: Int, memoId: Int): Option[Int]

  def getNextMemoId(uid: Int, memoId: Int): Option[Int]
}
