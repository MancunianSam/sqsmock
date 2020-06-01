package io.findify.sqsmock

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._

/**
  * Created by shutty on 3/29/16.
  */
class SQSService(port:Int, account:Int = 1) {
  val config: Config = ConfigFactory.parseMap(Map("akka.http.parsing.illegal-header-warnings" -> "off").asJava)
  implicit val system: ActorSystem = ActorSystem.create("sqsmock", config)
  def start():Unit = {
    val http = Http(system)
    val backend = new SQSBackend(account, port, system)
    val route =
      logRequest("request", Logging.DebugLevel) {
        pathPrefix(IntNumber) { _ =>
          path(Segment) { queueName =>
            post {
              formFieldMap { fields =>
                complete {
                  backend.process(fields + ("QueueUrl" -> s"http://localhost:$port/$account/$queueName"))
                }
              }
            }
          }
        } ~ post {
          formFieldMap { fields =>
            complete {
              backend.process(fields)
            }
          }
        }
      }
    Await.result(http.bindAndHandle(route, "localhost", 8001), Duration.Inf)
  }

  def shutdown():Unit = Await.result(system.terminate(), Duration.Inf)
  def block():Unit = Await.result(system.whenTerminated, Duration.Inf)
}

object SQSService {
  def main(args: Array[String]) {
    val sqs = new SQSService(8001, 1)
    sqs.start()
    sqs.block()
  }
}
