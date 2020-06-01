package io.findify.sqsmock.messages

import java.util.UUID

import io.findify.sqsmock.model.Queue

import scala.xml.Elem

/**
  * Created by shutty on 3/29/16.
  */
case class CreateQueueResponse(queue:Queue) extends Response {
  def toXML: Elem =
    <CreateQueueResponse>
      <CreateQueueResult>
        <QueueUrl>{queue.url}</QueueUrl>
      </CreateQueueResult>
      <ResponseMetadata>
        <RequestId>{uuid}</RequestId>
      </ResponseMetadata>
    </CreateQueueResponse>
}
