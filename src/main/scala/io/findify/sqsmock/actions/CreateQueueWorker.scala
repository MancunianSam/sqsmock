package io.findify.sqsmock.actions

import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import io.findify.sqsmock.messages.{CreateQueueResponse, ErrorResponse}
import io.findify.sqsmock.model.{Queue, QueueCache}

import scala.collection.mutable
import scala.util.matching.Regex

/**
  * Created by shutty on 3/29/16.
  */
class CreateQueueWorker(account:Long, port:Int, queues:mutable.Map[String,QueueCache], system:ActorSystem) extends Worker {
  val log = Logger(this.getClass, "create_queue_worker")


  val attributeFormat: Regex = """Attribute\.([0-9])\.([A-Za-z]+)""".r
  def process(fields:Map[String,String]): HttpResponse = fields.get("QueueName") match {
    case Some(queueName) =>
      val q = Queue(
        account,
        queueName,
        port,
        visibilityTimeout = fields.getOrElse("VisibilityTimeout", "30").toInt
      )
      log.debug(s"Creating queue $q, url=${q.url}")
      queues += (q.url -> new QueueCache(q))
      HttpResponse(StatusCodes.OK, entity = CreateQueueResponse(q).toXML.toString())
    case None =>
      log.warn("missing QueueName parameter, cannot create queue")
      HttpResponse(StatusCodes.BadRequest, entity = ErrorResponse("Sender", "InvalidParameterValue", "no QueueName parameter").toXML.toString())
  }
}
