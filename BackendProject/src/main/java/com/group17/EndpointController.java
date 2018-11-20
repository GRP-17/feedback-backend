package com.group17;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {

    // private static final String template = "Hello, %s!";
    // private final AtomicLong counter = new AtomicLong();

    private static final String response_template = "received {\n rating as:\n\n    %s\n\n text as:\n    %s\n}\n";

    // @RequestMapping("/greeting")
    // public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    //     return new Greeting(counter.incrementAndGet(),
    //                         String.format(template, name));
    // }

    @PostMapping("/feedback")
    public ResponseEntity<String> receiveFeedback(@RequestBody Feedback feedback) {
    	Application.getLogger().info("Endpoint called: [Rating:" + feedback.getRating() 
    									+ ", Text:" + feedback.getText() + "]");
        if(feedback.getRating() == null) {
        	Application.getLogger().error("Didn't provide rating");
            return new ResponseEntity("No rating provided\n", HttpStatus.BAD_REQUEST);
        } else if(feedback.getRating() < 1 || feedback.getRating() > 10) {
        	Application.getLogger().error("Rating wasn't between 1 and 10");
            return new ResponseEntity("Rating must be between 1 and 10\n", HttpStatus.BAD_REQUEST);
        } else {
        	String toReturn = String.format(response_template, feedback.getStars(), feedback.getText());
        	// TODO - Use this in database storage and also log the data storage
        	UUID uniqueId = UUID.randomUUID();

        	Application.getLogger().info("Successfully returned " + toReturn + " for " + uniqueId);
            return new ResponseEntity(toReturn, HttpStatus.OK);
        }
    }
	
}
