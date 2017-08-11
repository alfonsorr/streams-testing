name := "akka-streams-testing"

version := "1.0"

scalaVersion := "2.12.3"

lazy val akkaVersion = "2.5.3"
lazy val akkaHttpVersion = "10.0.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "io.monix" %% "monix" % "2.3.0",
  "org.scalatest" %% "scalatest" % "3.0.3" % "test"
)

        