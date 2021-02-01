name := "Spark.RestConsumer"

version := "1.0"

scalaVersion := "2.12.12"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

val value = "3.0.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % value

mainClass := Some("CurrencyJob")

libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % value

libraryDependencies += "org.apache.spark" %% "spark-core" % value

libraryDependencies += "org.apache.spark" %% "spark-sql" % value

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2"

libraryDependencies ++= {
  val liftVersion = "3.3.0"
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}

libraryDependencies += "com.typesafe" % "config" % "1.4.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"