package gatling;

import conf.BaseConf;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CheckPostcodesMeanResponseTime extends Simulation {

    int milliseconds = 3000;
    int usersPerSecond = 5;
    int maxDurationSeconds = 5;
    String postcode_feeder = "postcode_feeder.csv";
    String scenarioDescription = "(n)-users per second checking postcodes";
    String request = "/checkViaDatabase/";
    String path = request + "#{post_code}";

    /**
     * Build HTTP protocol for the request
     */	
    HttpProtocolBuilder httpProtocol = http
	.baseUrl(BaseConf.baseURL)
	.acceptHeader(BaseConf.textHTMLHeader)
	.doNotTrackHeader(BaseConf.doNotTrackHeader)
	.acceptLanguageHeader(BaseConf.acceptLanguageHeader)
	.acceptEncodingHeader(BaseConf.acceptEncodingHeader)
	.userAgentHeader(BaseConf.userAgentHeader);

    /**
     * Build the scenario to:
     * perform the GET request, verify the 200 response.
     */	
    ScenarioBuilder scn = scenario(scenarioDescription)
	.feed(csv(postcode_feeder).circular())
	.exec(
	    http(request)
	    .get(path)
	    .check(status().is(BaseConf.twoHundred))	
	)  
	.pause(5);

    /**
     * Specify users for the scenario and the final assertion(s)
     */
    {
        setUp(
            scn.injectOpen(constantUsersPerSec(usersPerSecond).during(maxDurationSeconds))
        )
       .assertions(global().responseTime().mean().lt(milliseconds))
       .protocols(httpProtocol);
    }
}
