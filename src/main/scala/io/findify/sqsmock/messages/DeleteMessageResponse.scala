package io.findify.sqsmock.messages

import scala.xml.Elem

/**
  * Created by shutty on 3/30/16.
  */
object DeleteMessageResponse extends Response {
  def toXML: Elem =
    <DeleteMessageResponse>
      <ResponseMetadata>
        <RequestId>
          {uuid}
        </RequestId>
      </ResponseMetadata>
    </DeleteMessageResponse>
}
