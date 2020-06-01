package io.findify.sqsmock.model

import scala.collection.mutable
import java.time.LocalDateTime

/**
  * Created by shutty on 3/30/16.
  */
class QueueCache(params:Queue) {
  case class MessageLease(when:LocalDateTime, msg:Message)

  val queue: mutable.Queue[Message] = mutable.Queue[Message]()
  val received: mutable.Map[String, MessageLease] = mutable.Map[String,MessageLease]()

  def enqueue(msg:Message): queue.type = queue += msg
  def enqueue(msgs:List[Message]): Unit = msgs.foreach(queue += _)
  def dequeue:Option[ReceivedMessage] = {
    val msg = queue.dequeueFirst(_ => true).map(ReceivedMessage(_))
    msg.foreach(m => received += (m.handle -> MessageLease(LocalDateTime.now(), m.message)))
    msg
  }
  def dequeue(count:Int):List[ReceivedMessage] = {
    (0 to count).flatMap(_ => dequeue).toList
  }
  def delete(handle:String): Boolean = received.get(handle) match {
    case Some(_) =>
      received -= handle
      true
    case None => false
  }
}
