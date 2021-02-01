package controllers

import akka.actor.ActorSystem
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import akka.actor.ActorSystem._
import akka.stream.ActorMaterializer
import play.api.libs.json.Json

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class CurrencyControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit val system = ActorSystem("TodoRoCurrencyControllerSpecuteSpec")
  implicit val mat = ActorMaterializer()
  "CurrencyController POST" should {

    "convert correctly" in {
      val controller = inject[CurrencyController]
      val body = List(CurrencyRequest("123", 10, "USD", "PLN"))
      val req = FakeRequest(POST, "/currency")
        .withHeaders("Content-type" -> "application/json")
        .withBody(Json.toJson(body))
      val resp = controller.convert().apply(req)

      status(resp) mustBe OK
      contentType(resp) mustBe Some("application/json")
      val bodyResp = contentAsJson(resp)
      val expected = Json.toJson(Array(CurrencyResponse("123", 10, 41, "USD", "PLN")))
      bodyResp mustEqual expected
    }

    "return 400 on empty body request" in {
      val request = FakeRequest(POST, "/currency")
      .withHeaders("Content-type" -> "application/json")
      val resp = route(app, request).get

      status(resp) mustBe BAD_REQUEST
      contentType(resp) mustBe Some("application/json")
      contentAsString(resp) must include("Invalid Json")
    }
  }
}
