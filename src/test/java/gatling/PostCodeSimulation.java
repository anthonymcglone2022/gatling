package gatling;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PostCodeSimulation extends Simulation { // 3
	
	
  //String URL = "http://localhost:9191/checkViaDatabase/W5 1AT";
  String URL = "http://localhost:9191";

  HttpProtocolBuilder httpProtocol = http // 4
    .baseUrl(URL) // 5
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  ScenarioBuilder scn = scenario("PostCodeSimulation") // 7
    .feed(csv("postcode_feeder.csv").circular())
	.exec(
    		http("checkViaDatabase") // 8
    		.get("/checkViaDatabase/" + "${post_code}")
    		.check(status().is(200))
    		//.check(bodyString().saveAs("responseBody"))
    )
    .exec(session -> {
        //System.out.println("Response Body:");
        //System.out.println(session.getString("responseBody"));
        return session;
    })    
    .pause(5); // 10

  {
    setUp( // 11
      scn.injectOpen(constantUsersPerSec(5).during(5)) // 12
    ).protocols(httpProtocol); // 13
  }
}