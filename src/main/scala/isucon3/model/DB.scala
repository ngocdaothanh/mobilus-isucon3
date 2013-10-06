package isucon3.model

import java.sql.{Connection, PreparedStatement, ResultSet, Statement, Timestamp}
import scala.util.control.NonFatal
import scala.collection.mutable.ArrayBuffer

import com.tristanhunt.knockoff.DefaultDiscounter._
import com.tristanhunt.knockoff._

import org.h2.jdbcx.JdbcConnectionPool
import xitrum.Logger

object DB extends Logger {
  var cp = JdbcConnectionPool.create("jdbc:h2:./db", "sa", "sa")

  def reset(): Unit = synchronized {
    cp.dispose()

    import scala.sys.process._
    Process("cp db.h2.db_benchmark db.h2.db").!

    cp = JdbcConnectionPool.create("jdbc:h2:./db", "sa", "sa")
  }

  def getConnection() = synchronized {
    cp.getConnection()
  }

  def signin(username: String, password: String): Option[User] = {
    var con: Connection        = null
    var s:   PreparedStatement = null
    var r:   ResultSet         = null
    try {
      con = getConnection()
      s   = con.prepareStatement("SELECT id, password, salt FROM users WHERE username=? LIMIT 1")
      s.setString(1, username)

      r = s.executeQuery()
      if (r.next()) {
        val id         = r.getInt("id")
        val dbPassword = r.getString("password")
        val salt       = r.getString("salt")

        if (dbPassword == Sha.hash256(salt + password)) {
          updateUserLastAccess(id)
          Some(User(id, username))
        } else {
          None
        }
      } else {
        None
      }
    } catch {
      case NonFatal(e) =>
        logger.error("signin", e)
        None
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  def countPublicMemos(): Int = {
    var con: Connection = null
    var s:   Statement  = null
    var r:   ResultSet  = null
    try {
      con = getConnection()
      s   = con.createStatement()
      r   = s.executeQuery("SELECT count(*) AS c FROM memos WHERE is_private=0")

      r.next()
      val ret = r.getInt("c")
      ret
    } catch {
      case NonFatal(e) =>
        logger.error("countPublicMemos", e)
        0
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  def getRecentPublicMemos(page: Int): Seq[Memo] = {
    var con: Connection = null
    var s:   Statement  = null
    var r:   ResultSet  = null
    try {
      con = getConnection()
      s   = con.createStatement()
      r   =
        if (page <= 0)
          s.executeQuery("SELECT * FROM memos WHERE is_private=0 ORDER BY id DESC LIMIT 100")
        else
          s.executeQuery("SELECT * FROM memos WHERE is_private=0 ORDER BY id DESC LIMIT 100 OFFSET " + (page * 100))

      extractMemos(r)
    } catch {
      case NonFatal(e) =>
        logger.error("getRecentPublicMemos", e)
        null
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  def getMyMemos(userId: Int): Seq[Memo] = {
    var con: Connection = null
    var s:   Statement  = null
    var r:   ResultSet  = null
    try {
      con = getConnection()
      s   = con.createStatement()
      r   = s.executeQuery("SELECT * FROM memos WHERE user=" + userId + " ORDER BY id DESC")
      extractMemos(r)
    } catch {
      case NonFatal(e) =>
        logger.error("getMyMemos", e)
        null
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  def addMemo(user: User, content: String, isPrivate: Boolean): Int = {
    var con: Connection         = null
    var s:   PreparedStatement  = null
    var r:   ResultSet          = null
    try {
      var contentHtml = ""

      val contentLine = content.lines
      val title = contentLine.next()
      contentHtml += toXHTML(knockoff(title)).toString
      while(contentLine.hasNext){
        contentHtml += toXHTML(knockoff(contentLine.next())).toString
      }


      con = getConnection()
      s   = con.prepareStatement("INSERT INTO memos (user, username, title, content, is_private, created_at) VALUES (?, ?, ?, ?, ?, ?)")
      s.setInt      (1, user.id)
      s.setString   (2, user.username)
      s.setString   (3, title)
      s.setString   (4, contentHtml)
      s.setInt      (5, if (isPrivate) 1 else 0)
      s.setTimestamp(6, new Timestamp(System.currentTimeMillis()))

      s.executeUpdate()

      r = s.getGeneratedKeys()
      if (r.next()) r.getInt(1) else 0
    } catch {
      case NonFatal(e) =>
        logger.error("addMemo", e)
        0
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  def getMemo(memoId: Int): Option[Memo] = {
    var con: Connection = null
    var s:   Statement  = null
    var r:   ResultSet  = null
    try {
      con = getConnection()
      s   = con.createStatement()
      r   = s.executeQuery("SELECT * FROM memos WHERE id=" + memoId)

      extractMemos(r).headOption
    } catch {
      case NonFatal(e) =>
        logger.error("getMemo", e)
        null
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  def getPrevMemoId(uid: Int, memoId: Int): Option[Int] = {
    var con: Connection = null
    var s:   Statement  = null
    var r:   ResultSet  = null
    try {
      con = getConnection()
      s   = con.createStatement()
      r   = s.executeQuery("SELECT * FROM memos WHERE user = " + uid + " id < " + memoId + " ORDER BY id LIMIT 1")
      extractMemos(r).headOption.map(_.id)
    } catch {
      case NonFatal(e) =>
        logger.error("getPrevMemoId", e)
        None
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  def getNextMemoId(uid: Int, memoId: Int): Option[Int] = {
    var con: Connection = null
    var s:   Statement  = null
    var r:   ResultSet  = null
    try {
      con = getConnection()
      s   = con.createStatement()
      r   = s.executeQuery("SELECT * FROM memos WHERE user = " + uid + " id > " + memoId + " ORDER BY id LIMIT 1")
      extractMemos(r).headOption.map(_.id)
    } catch {
      case NonFatal(e) =>
        logger.error("getNextMemoId", e)
        None
    } finally {
      if (s != null) s.close()
      if (r != null) r.close()
      if (con != null) con.close()
    }
  }

  //----------------------------------------------------------------------------

  private def updateUserLastAccess(userId: Int) {
    var con: Connection = null
    var s:   Statement  = null
    try {
      con = getConnection()
      s   = con.createStatement()
      s.executeUpdate("UPDATE users SET last_access=now() WHERE id=" + userId)
    } catch {
      case NonFatal(e) =>
        logger.error("updateUserLastAccess", e)
    } finally {
      if (s != null) s.close()
      if (con != null) con.close()
    }
  }

  private def extractMemos(r: ResultSet): Seq[Memo] = {
    val ret = ArrayBuffer[Memo]()
    while (r.next()) {
      val id          = r.getInt("id")
      val title       = r.getString("title")
      val contentHtml = r.getString("content")
      val createdAt   = r.getTime("created_at").toString
      val uid         = r.getInt("user")
      val username    = r.getString("username")
      ret.append(Memo(id, false, title, contentHtml, createdAt, uid, username))
    }
    ret
  }
}
