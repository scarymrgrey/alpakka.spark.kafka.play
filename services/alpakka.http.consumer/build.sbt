name := "alpakka.http.consumer"

version := "0.1"

scalaVersion := "2.13.1"

val ScalaTestVersion = "3.1.4"
val AkkaVersion = "2.6.9"
val AkkaHttpVersion = "10.1.12"
val AlpakkaVersion = "2.0.1"
val AlpakkaKafkaVersion = "2.0.5"

val dependencies = List(
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % AlpakkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % AlpakkaKafkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  // Used from Scala
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  // Used from Java
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.10.5",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.10.5",

  "org.testcontainers" % "kafka" % "1.14.3",

  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

libraryDependencies += "net.liftweb" %% "lift-webkit" % "3.4.3"

libraryDependencies ++= dependencies