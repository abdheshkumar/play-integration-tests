package controllers

/**
 * Created by abdhesh on 10/10/15.
 */

import play.api.libs.ws.WS
import play.api.test._

class ApplicationIntegrationSpec extends PlaySpecification {

  "Application" should {
    "be reachable" in new WithServer {
      val response = await(WS.url("http://localhost:" + port).get()) //1

      response.status must equalTo(OK) //2
      response.body must contain("Semaphore Community Library") //3
    }
  }
}
