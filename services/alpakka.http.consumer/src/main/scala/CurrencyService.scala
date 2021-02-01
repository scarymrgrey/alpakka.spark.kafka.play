import akka.http.scaladsl.model.{ContentType, HttpMethods, HttpRequest, MediaTypes}
import net.liftweb.json.DefaultFormats

trait CurrencyService {
  def convert(req : String): HttpRequest
}

object CurrencyServiceConverter extends CurrencyService {
  implicit val formats: DefaultFormats.type = DefaultFormats
  def convert(req : String): HttpRequest = {
    val charset: ContentType.WithFixedCharset = ContentType(MediaTypes.`application/json`)
    HttpRequest(uri = "http://172.25.0.22:9009/currency")
      .withMethod(HttpMethods.POST)
      .withEntity(charset,"[" + req + "]")
  }
}
