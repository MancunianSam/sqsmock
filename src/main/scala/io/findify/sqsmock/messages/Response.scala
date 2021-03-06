package io.findify.sqsmock.messages

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.UUID

/**
  * Created by shutty on 3/29/16.
  */
trait Response {
  def hex(bytes:Array[Byte]): String = bytes.map("%02X" format _).mkString.toLowerCase
  def md5(value:String): Array[Byte] = MessageDigest.getInstance("MD5").digest(value.getBytes)
  def uuid: String = UUID.randomUUID.toString
}
