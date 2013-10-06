package isucon3.model

import java.security._

// https://gist.github.com/avilches/750151
object Sha {
  def hash256(data: String) = {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(data.getBytes());
    bytesToHex(md.digest())
  }

  def bytesToHex(bytes: Array[Byte]) = {
    val result = new StringBuffer
    for (byt <- bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1))
    result.toString
  }
}
