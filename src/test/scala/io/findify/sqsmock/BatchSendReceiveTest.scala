package io.findify.sqsmock

import com.amazonaws.services.sqs.model.{ReceiveMessageRequest, SendMessageBatchRequestEntry}
import org.scalatest.flatspec.AnyFlatSpec

import scala.jdk.CollectionConverters._
/**
  * Created by shutty on 3/30/16.
  */
class BatchSendReceiveTest extends AnyFlatSpec with SQSStartStop {
  val queue = "http://localhost:8001/123/foo"
  "sqs mock" should "send messages in batch" in {
    client.createQueue("foo")
    val batch = List(
      new SendMessageBatchRequestEntry("1", "hello"),
      new SendMessageBatchRequestEntry("2", "world")
    )
    val result = client.sendMessageBatch(queue, batch.asJava)
    assert(result.getFailed.isEmpty)
    assert(result.getSuccessful.size == 2)
  }
  it should "receive message in batch" in {
    val request = new ReceiveMessageRequest()
    request.setMaxNumberOfMessages(10)
    request.setQueueUrl(queue)
    val result = client.receiveMessage(request)
    assert(result.getMessages.size() == 2)
  }

}
