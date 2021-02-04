import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.OK
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import net.liftweb.json.DefaultFormats
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, ByteArraySerializer, StringDeserializer, StringSerializer}
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.Future

case class CurrencyRequest(id: String, value: Double, from_currency: String, to_currency: String)

object JsonFormats {
  // Spray JSON conversion setup (3)
  implicit val movieFormat: JsonFormat[CurrencyRequest] = jsonFormat4(CurrencyRequest)
}


object Main extends App {

  def extractEntityData(response: HttpResponse): Source[ByteString, _] =
    response match {
      case HttpResponse(OK, _, entity, _) => entity.dataBytes
      case notOkResponse =>
        Source.failed(new RuntimeException(s"illegal response $notOkResponse"))
    }

  implicit val actorSystem: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty, "alpakka-samples")
  implicit val formats: DefaultFormats.type = DefaultFormats

  import actorSystem.executionContext
  val consumerConfig = actorSystem.settings.config.getConfig("our-kafka-consumer")
  val finalConfig = actorSystem.settings.config.getConfig("settings")

  val uuid = () => java.util.UUID.randomUUID().toString
  val cs: CurrencyService = CurrencyServiceConverter


  val bootstrapServers = sys.env.get("BOOTSTRAP_SERVERS").getOrElse(finalConfig.getString("bootstrap-servers"))
  val currencyUrl = sys.env.get("CURRENCY_URL").getOrElse(finalConfig.getString("currency-api"))
  val paraLevel : Int = sys.env.get("PARA_LEVEL").getOrElse(finalConfig.getInt("para-level")).toString.toInt

  val converter = cs.convert(currencyUrl)(_)

  val kafkaConsumerSettings =
    ConsumerSettings(consumerConfig, new StringDeserializer, new ByteArrayDeserializer)
      .withBootstrapServers(bootstrapServers)
      .withGroupId("group-" + uuid())
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

  val kafkaProducerSettings = ProducerSettings(actorSystem.toClassic, new StringSerializer, new ByteArraySerializer)
    .withBootstrapServers(bootstrapServers)

  val control = Consumer
    .atMostOnceSource(kafkaConsumerSettings, Subscriptions.topics("currency_requests_alpakka"))
    .map { consumerRecord =>
      val jsonString = consumerRecord.value().map(_.toChar).mkString
      jsonString
    }
    .map { writeResult => // (8)
      converter(writeResult)
    }
    .mapAsync(paraLevel)(Http()(actorSystem.toClassic).singleRequest(_))
    .flatMapConcat(extractEntityData)


  val future: Future[Done] =
    control
      .map { elem =>
        val array: Array[Byte] = elem.toArray
        new ProducerRecord[String, Array[Byte]]("alpakka-produced", array)
      }
      .runWith(Producer.plainSink(kafkaProducerSettings))

  future.map { _ =>
    println("Done!")
    actorSystem.terminate()
  }

}

