package com.group17;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group17.util.Tuple;

@RestController
public class EndpointController {
    private static final String response_template = "Received:\n-Rating: %s\n-Text: %s\n";

    @PostMapping("/feedback")
    public ResponseEntity<String> receiveFeedback(@RequestBody Feedback feedback) {
    	Application.getLogger().info("Endpoint called: [Rating:" + feedback.getRating() 
    									+ ", Text:" + feedback.getText() + "]");
    	
    	Tuple<String, HttpStatus> responseTuple = getResponse(feedback);
    	
//    	if(responseTuple.getV().equals(HttpStatus.OK))
//    	{
//        	TODO - Use this in database storage and also log the data storage
//        	UUID uniqueId = UUID.randomUUID();
//    	}
    	
        return new ResponseEntity<String>(responseTuple.getK(), responseTuple.getV());
    }
    
    protected Tuple<String, HttpStatus> getResponse(Feedback feedback) {
        if(feedback.getRating() == null) {
        	Application.getLogger().error("Didn't provide rating");
        	return new Tuple<String, HttpStatus>("No rating provided\n", HttpStatus.BAD_REQUEST);
        } else if(feedback.getRating() < 1 || feedback.getRating() > 10) {
        	Application.getLogger().error("Rating wasn't between 1 and 10");
        	return new Tuple<String, HttpStatus>("Rating wasn't between 1 and 10\n", HttpStatus.BAD_REQUEST);
        } else {
        	String toReturn = String.format(response_template, feedback.getStars(), feedback.getText());
        	Application.getLogger().info("Successfully returned " + toReturn);
            return new Tuple<String, HttpStatus>(toReturn, HttpStatus.OK);
        }
    }
	
}
