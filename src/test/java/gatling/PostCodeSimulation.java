package gatling;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PostCodeSimulation extends Simulation {
	
  String URL = "http://localhost:9191";
  int limit = 3000;
  int usersPerSecond = 5;
  int maxDurationSeconds = 5;
  String csv = "postcode_feeder.csv";

  HttpProtocolBuilder httpProtocol = http
    .baseUrl(URL)
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  ScenarioBuilder scn = scenario("(n)-users per second checking postcodes")
    .feed(csv(csv).circular())
	.exec(
		http("checkViaDatabase")
		.get("/checkViaDatabase/" + "#{post_code}")
		.check(status().is(200))	
    )  
    .pause(5);

  {
    setUp(
      scn.injectOpen(constantUsersPerSec(usersPerSecond).during(maxDurationSeconds))
    )
    .assertions(global().responseTime().mean().lt(limit))
    .protocols(httpProtocol);
  }
}