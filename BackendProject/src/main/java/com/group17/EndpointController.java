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
        if(feedback.getRating() == null) {
            return new ResponseEntity("no rating provided\n", HttpStatus.BAD_REQUEST);
        } else if(feedback.getRating() < 1 || feedback.getRating() > 10) {
            return new ResponseEntity("rating cannot be less than 1 star or more than 10 stars\n", HttpStatus.BAD_REQUEST);
        } else {
        	
        	// TODO - Use this in database storage
        	UUID uniqueId = UUID.randomUUID();
        	
            return new ResponseEntity(String.format(response_template, feedback.getStars(), feedback.getText()), HttpStatus.OK);
        }
    }
	
}
