import io.gatling.core.Predef._
import io.gatling.http.Predef._

import  java.util.concurrent._
import java.util.UUID

class MySimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://127.0.0.1:8080")

  val scn = scenario("Load data")
    .feed(Iterator.continually(Map("user" -> UUID.randomUUID())))
    .feed(Iterator.continually(Map("resource" -> ThreadLocalRandom.current.nextInt(10))))
    .feed(Iterator.continually(Map("pause" -> ThreadLocalRandom.current.nextInt(60))))
    .feed(Iterator.continually(Map("interest" -> ThreadLocalRandom.current.nextGaussian())))
    .exec(http("request_1")
      .get("/java-webapp/api?user=${user}&resource=${resource}"))
    .pause("${pause}")
    .exec(http("request_2")
      .post("/java-webapp/api?user=${user}&interest=${interest}"))


  setUp(scn.inject(rampUsers(100000) during (600)).protocols(httpProtocol))
}