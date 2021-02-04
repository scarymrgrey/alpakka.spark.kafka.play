
import java.util.Properties
import org.apache.kafka.clients.producer._

object Producer {
  def main(args: Array[String]): Unit = {
    val bootstrapServers = sys.env.get("BOOTSTRAP_SERVERS").getOrElse("172.25.0.12:29092")
    val delay: Boolean = sys.env.get("DELAY").getOrElse(true).toString.toBoolean
    println("Start producing random requests...")
    writeToKafka("currency_requests", bootstrapServers, delay)
  }

  def writeToKafka(topic: String, servers: String, delay: Boolean): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", servers)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val r = scala.util.Random
    while (true) {
      val value = "{\"value\":" + r.nextInt(1000) + ",\"from_currency\":\"PLN\",\"to_currency\":\"USD\"}"
      val record = new ProducerRecord[String, String](topic, "key", value)
      producer.send(record)
      if (delay)
        Thread.sleep(1000)
    }

    producer.close()
  }
}
