package org.alfiler

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await

class HttpRequestTest extends FlatSpec with Matchers{

  import scala.concurrent.duration._

  implicit val actorSystem = ActorSystem("stream-test")
  implicit val materializer = ActorMaterializer()


  private val source = Source(List("https://www.carrefour.es/ps4-slim-500gb-playstation/2052054568/p"))

  private val httpRequest = Flow[String].map(s => (HttpRequest(uri = s),s))

  "jander" should "jarenawer" in {
    val f = Http().superPool[String]()

    val algo = source.via(httpRequest).via(f)
      .mapAsync(5)(t => t._1.get.entity.dataBytes.runFold(ByteString(""))(_ ++ _))
      .map(_.utf8String).runWith(Sink.foreach(println))

    Await.result(algo, 3.seconds)
  }
}
