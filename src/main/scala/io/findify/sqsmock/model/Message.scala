package io.findify.sqsmock.model

import java.util.UUID



/**
  * Created by shutty on 3/29/16.
  */
case class Message(id:String, body:String, attributes:Map[String,String])

object Message {
  def apply(body:String) = new Message(
    id = UUID.randomUUID.toString,
    body = body,
    attributes = Map("SentTimestamp" -> System.currentTimeMillis().toString)
  )
}