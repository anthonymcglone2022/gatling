package gatling;

import conf.BaseConf;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CreatePostcodesMeanResponseTime extends Simulation {
	
	int milliseconds = 100;
	int usersPerSecond = 10;
	int maxDurationSeconds = 10;	
	String postcode_feeder = "postcode_feeder.json";
	String scenarioDescription = "(n)-users per second creating postcodes";
	String path = "/createPostCode/";
	String template = "{\"code\": \"#{code}\",\"inuse\": \"#{inuse}\",\"district\": \"#{district}\"}";

  	HttpProtocolBuilder httpProtocol = http
	    .baseUrl(BaseConf.baseURL)
	    .acceptHeader(BaseConf.jsonHeader)
	    .contentTypeHeader(BaseConf.jsonHeader)
	    .doNotTrackHeader(BaseConf.doNotTrackHeader);
  
  	FeederBuilder<Object> jsonFeeder = jsonFile(postcode_feeder).circular();

  	ScenarioBuilder scn = scenario(scenarioDescription)
          .feed(jsonFeeder)  
          .exec(http(path)
                  .post(path)
                  .body(StringBody(template)).asJson()
                  .check(status().is(BaseConf.twoHundred))
           )
          
          .pause(1);

  	{
		setUp(
		  scn.injectOpen(constantUsersPerSec(usersPerSecond).during(maxDurationSeconds))
		)
		.assertions(global().responseTime().mean().lt(milliseconds))
		.protocols(httpProtocol);
	}
  
}