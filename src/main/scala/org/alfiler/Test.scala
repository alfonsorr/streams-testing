package org.alfiler

import monix.eval.Task
import monix.reactive.Observable

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import monix.execution.Scheduler.Implicits.global

object Timer {
  def withTimer(f: () => Unit) = {
    (1 to 3).foreach(_ => f())
    println((1 to 5).map( _ => {
      val start = System.nanoTime()
      f()
      val end = System.nanoTime()
      end - start
    }).map(a => {println(a); a}).sum)
  }
}

object Test extends App {
  val observable: Observable[(String, Int)] = Observable
    .repeat(("A", 1), ("B", 2), ("C", 3), ("D", 4))
    .take(40000000)
    .groupBy(_._1)
    .mapAsync { group =>
      group
        .foldLeftL(0) { case (sum, (character, nextNum)) =>
          sum + nextNum
        }
        .map( sum => (group.key, sum) )
    }


  Timer.withTimer(() => Await.result(observable.foreach(println), Duration.Inf))
}


object Test2 extends App {
  val observable: Task[Map[String, Int]] = Observable
    .repeat(("A", 1), ("B", 2), ("C", 3), ("D", 4))
    .take(40000000)
    .foldLeftL(Map[String, Int]())((acum, e) => acum + (e._1 -> (e._2 + acum.getOrElse(e._1, 0))))

  Timer.withTimer(() => Await.result(observable.foreach(println), Duration.Inf))
}

object ObservablePlus {
  implicit def getFromObservable[A](o: Observable[A]): ObservablePlus[A] = new ObservablePlus(o)
}

case class ObservablePlus[A](o:Observable[A]) {
  def foldLeftByL[K,V](key:A=>K)(zero:V)(op:(V,A) => V):Task[Map[K,V]] = {
    o.foldLeftL(Map[K,V]())((acum, next) => {
      val keyNext = key(next)
      acum + (keyNext -> op(acum.getOrElse(keyNext, zero),next))
    })
  }
}

object Test3 extends App {
  import ObservablePlus.getFromObservable
  val observable: Task[Map[String, Int]] = Observable
    .repeat(("A", 1), ("B", 2), ("C", 3), ("D", 4))
    .take(40000000)
    .foldLeftByL(_._1)(0)((a,b) => a + b._2)

  Timer.withTimer(() => Await.result(observable.foreach(println), Duration.Inf))
}