package io.findify.sqsmock.model

import scala.collection.mutable
import java.time.LocalDateTime

/**
 * Created by shutty on 3/30/16.
 */
class QueueCache(params: Queue) {

  case class MessageLease(when: LocalDateTime, msg: Message)

  val queue: mutable.Queue[Message] = mutable.Queue[Message]()

  def enqueue(msg: Message): queue.type = queue += msg

  def enqueue(msgs: List[Message]): Unit = msgs.foreach(queue += _)

  def dequeue: Option[ReceivedMessage] =
    queue.dequeueFirst(_ => true).map(ReceivedMessage(_))

  def dequeue(count: Int): List[ReceivedMessage] = {
    (0 to count).flatMap(_ => dequeue).toList
  }

  def delete(handle: String) = {
    def matchHandle(m: Message) = {
      m.attributes("ReceiptHandle") == handle
    }

    queue.find(matchHandle) match {
      case Some(_) => queue.removeAll(matchHandle).nonEmpty
      case None => false

    }
  }
}
