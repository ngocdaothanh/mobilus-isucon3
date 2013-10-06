package isucon3.model

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}
import scala.util.control.NonFatal
import scala.collection.mutable.ArrayBuffer

import org.h2.jdbcx.JdbcConnectionPool
import xitrum.Logger

object DB extends Logger {
  val cp = JdbcConnectionPool.create("jdbc:h2:./db", "sa", "sa")

  def signin(username: String, password: String): Option[User] = {
    var con: Connection        = null
    var s:   PreparedStatement = null
    var r:   ResultSet         = null
    try {
      con = cp.getConnection()
      s   = con.prepareStatement("SELECT id, password, salt FROM users WHERE username=? LIMIT 1")
      s.setString(1, username)

      r = s.executeQuery()
      if (r.next()) {
        val id         = r.getInt("id")
        val dbPassword = r.getString("password")
        val salt       = r.getString("salt")

        if (dbPassword == Sha.hash256(salt) + password) {
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
      con = cp.getConnection()
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

  def getRecentPublicMemos(): Seq[Memo] = {
    var con: Connection = null
    var s:   Statement  = null
    var r:   ResultSet  = null
    try {
      con = cp.getConnection()
      s   = con.createStatement()
      r   = s.executeQuery("SELECT * FROM memos WHERE is_private=0 ORDER BY created_at DESC, id DESC LIMIT 100")

      val ret = ArrayBuffer[Memo]()
      while (r.next()) {
        val id          = r.getInt("id")
        val username    = r.getString("username")
        val title       = r.getString("title")
        val contentHtml = r.getString("content")
        val createdAt   = r.getTime("created_at").toString
        ret.append(Memo(id, username, false, title, contentHtml, createdAt))
      }
      ret
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
}
