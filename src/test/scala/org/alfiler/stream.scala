package org.alfiler

import java.time.LocalTime
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.testkit.scaladsl.TestSink
import akka.stream.{ActorMaterializer, ThrottleMode}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, FiniteDuration}

class stream extends FlatSpec with Matchers{

  case class Tick()

  implicit val actorSystem = ActorSystem("stream-test")
  implicit val materializer = ActorMaterializer()

  "stream" should "throttle" in {
    val nEvents = 1
    val duration = Duration(10,"second")
    val mat = Source(1 to 11).throttle(nEvents, FiniteDuration(duration.toNanos, TimeUnit.NANOSECONDS), 0, ThrottleMode.Shaping).runWith(TestSink.probe[Int])
    mat.requestNext(FiniteDuration(11,"second"))
    mat.requestNext(FiniteDuration(11,"second"))
  }

  it should "throttle right" in {
    val nEvents = 1
    val duration = Duration(1,"second")
    val mat = Source(1 to 10)
      .throttle(nEvents, FiniteDuration(duration.toNanos, TimeUnit.NANOSECONDS), 0, ThrottleMode.Shaping)
        .map(_ => LocalTime.now().toString)
      .runWith(Sink.foreach(println))
    Await.result(mat, duration * 11)
  }


  it should "throttle correctly" in {
    import scala.concurrent.duration._
    val mat = Source(1 to 1000).throttle(100, 1.seconds,1,ThrottleMode.Shaping).map(a => {println(s"algo: $a");a}).conflate((a,b) => b).throttle(3, 5.seconds,1,ThrottleMode.Shaping).runWith(Sink.foreach(println))

    Await.result(mat, 100.seconds)
  }
}
