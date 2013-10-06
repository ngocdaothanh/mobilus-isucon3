package isucon3

import java.sql.{Connection, DriverManager, Statement}
import isucon3.model.DB
import java.text.SimpleDateFormat

import com.tristanhunt.knockoff.DefaultDiscounter._
import com.tristanhunt.knockoff._

object Mysql2H2 {
  def main(args: Array[String]) {
    convert()
  }

  //----------------------------------------------------------------------------
  // These are "public" so that we can call them from SBT console

  def convert() {
    Class.forName("com.mysql.jdbc.Driver")
    val my = DriverManager.getConnection("jdbc:mysql://localhost:3306/isucon?user=root&password=root&useUnicode=true&characterEncoding=UTF-8")
    val h2 = DB.cp.getConnection()

    resetH2(h2)
    convertMemos(my, h2)

    h2.close()
    my.close()
  }

  def resetH2(h2: Connection) {
    var s = h2.createStatement()
    s.execute(scala.io.Source.fromFile("db.sql").mkString)
    s.close()
  }

  def convertMemos(my: Connection, h2: Connection) {
    var s = my.createStatement()
    var s2 = h2.createStatement()
    val r = s.executeQuery("SELECT memos.* ,users.username FROM memos, users WHERE memos.user = users.id ORDER BY id")

    while (r.next()) {
      val id            = r.getInt("id")
      val user          = r.getInt("user")
      val username      = r.getString("username")
      val content       = r.getString("content")
      val isPrivate     = r.getInt("is_private")
      val created_at    = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(r.getDate("created_at")).toString()
      val updated_at    = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(r.getDate("updated_at")).toString()

      var contentHtml = ""

      //memo["content"].split(/\r?\n/).first
      val contentLine = content.lines
      val title = contentLine.next()
      contentHtml += toXHTML(knockoff(title)).toString
      while(contentLine.hasNext){
        contentHtml += toXHTML(knockoff(contentLine.next())).toString
      }

      var ps = h2.prepareStatement("INSERT INTO memos(id, title, content, is_private, created_at, updated_at, user, username)VALUES(?,?,?,?,?,?,?,?)")
      ps.setInt(1, id)
      ps.setString(2, title)
      ps.setString(3, contentHtml)
      ps.setInt(4, isPrivate)
      ps.setString(5, created_at)
      ps.setString(6, updated_at)
      ps.setInt(7, user)
      ps.setString(8, username)
      ps.execute()
      ps.close()
    }
    s.close()
    s2.close()
  }

  def convertUsers(h2: Connection, my: Connection) {
    var s = my.createStatement()
    var s2 = h2.createStatement()
    val r = s.executeQuery("SELECT * FROM users ORDER BY id")
    while (r.next()) {
      val id          = r.getInt("id")
      val username    = r.getString("username")
      val password    = r.getString("password")
      val salt        = r.getString("salt")
      val last_access = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(r.getDate("last_access")).toString()

      var ps = h2.prepareStatement("INSERT INTO users(id, username, password, password, password)VALUES(?,?,?,?,?)")
      ps.setInt(1, id)
      ps.setString(2, username)
      ps.setString(3, password)
      ps.setString(4, salt)
      ps.setString(5, last_access)
      ps.execute()
      ps.close()
    }
    s.close()
    s2.close()
  }
}
