#!/bin/bash 

cd /services/kafka.producer ; sbt compile ; sbt run &

cd /services/play.currencyapi ; sbt compile ; sbt dist &

cd /services/alpakka.http.consumer ; sbt compile ; sbt run &

cd /services/spark.restconsumer ; sbt compile ; sbt run
