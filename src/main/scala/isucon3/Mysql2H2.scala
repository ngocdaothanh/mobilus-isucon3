package isucon3

import java.sql.{Connection, DriverManager, Statement}
import scala.util.control.NonFatal
import org.h2.jdbcx.JdbcConnectionPool

object Mysql2H2 {
  def main(args: Array[String]) {
    convert()
  }

  //----------------------------------------------------------------------------
  // These are "public" so that we can call them from SBT console

  def convert() {
    Class.forName("com.mysql.jdbc.Driver")
    val my = DriverManager.getConnection("jdbc:mysql://localhost:3306/isucon3?user=root&password=root&useUnicode=true&characterEncoding=UTF-8")
    val h2 = JdbcConnectionPool.create("jdbc:h2:./db", "sa", "sa").getConnection()

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
    val r = s.executeQuery("SELECT * FROM memos ORDER BY id LIMIT 100 OFFSET 0")
    while (r.next()) {
      val id        = r.getInt("id")
      val content   = r.getString("content")
      val isPrivate = r.getInt("is_private")


      val title = content  // FIXME


      println(id)
    }
    s.close()
  }

  def convertUsers(h2: Connection, my: Connection) {

  }
}
