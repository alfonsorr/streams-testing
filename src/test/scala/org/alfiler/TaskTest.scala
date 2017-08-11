package org.alfiler

import org.scalatest.{FlatSpec, Matchers}
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.{Await, Future}

// A Future type that is also Cancelable
import monix.execution.CancelableFuture

// Task is in monix.eval
import monix.eval.Task
import scala.util.{Success, Failure}

import scala.concurrent.duration._

class TaskTest extends FlatSpec with Matchers{

  "Task" should "do something" in {

    val t1 = Task{println("jander"); 1+1}

    val t2 = t1.map(_ + 5)

    val t3 = t1.map(_ - 1)


    println(Await.result(t2.runAsync, 2.seconds))
    println(Await.result(t3.runAsync, 2.seconds))

  }

  it  should "do something with futures" in {

    val t1 = Task{println("jander"); 1+1}.delayExecution(1.seconds).runAsync

    val t2 = t1.map(_ + 5)

    val t3 = t1.map(_ - 1)

    //t1.cancel()
    //t3.cancel()
    println(Await.result(t3, 2.seconds))
    println(Await.result(t2, 2.seconds))

  }


  "Future" should "do something" in {

    val t1 = Future{println("jander"); 1+1}

    val t2 = t1.map(_ + 5)

    val t3 = t1.map(_ - 1)

    println(Await.result(t2, 2.seconds))
    println(Await.result(t3, 2.seconds))

  }

}
