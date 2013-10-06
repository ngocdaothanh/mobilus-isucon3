package isucon3.model

import org.h2.jdbcx.JdbcConnectionPool

object DB {
  val cp = JdbcConnectionPool.create("jdbc:h2:./db", "sa", "sa")

}
