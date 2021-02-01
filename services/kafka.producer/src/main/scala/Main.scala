
import java.util.Properties
import org.apache.kafka.clients.producer._

object Producer {
  def main(args: Array[String]): Unit = {
    print("bootstrap.servers: ")
    //val servers = scala.io.StdIn.readLine()
    val servers = "172.25.0.12:29092"
    println("Start producing random requests...")
    writeToKafka("currency_requests", servers)
  }

  def writeToKafka(topic: String, servers: String): Unit = {
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
    }

    producer.close()
  }
}
