package io.findify.sqsmock

import com.amazonaws.auth.AnonymousAWSCredentials
import com.amazonaws.services.sqs.AmazonSQSClient
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec

/**
  * Created by shutty on 3/31/16.
  */
trait SQSStartStop extends AnyFlatSpec with BeforeAndAfterAll {
  var sqs:SQSService = _
  var client:AmazonSQSClient = _
  override def beforeAll: Unit = {
    sqs = new SQSService(8001, 123)
    sqs.start()
    client = new AmazonSQSClient(new AnonymousAWSCredentials())
    client.setEndpoint("http://localhost:8001")
  }
  override def afterAll: Unit = {
    sqs.shutdown()
  }
}
