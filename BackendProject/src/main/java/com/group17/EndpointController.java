package com.group17;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group17.util.Tuple;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/feedback", produces = "application/hal+json")
public class EndpointController {
    private final String response_template = "Feedback:\n-Rating: %s\n-Text: %s\n";
    private final String full_response_template = "%s -Link: %s\n";

    @Autowired
    private FeedbackRepository feedbackDb;

    @PostMapping(headers = "Accept=application/json")
    public ResponseEntity<String> createFeedback(HttpServletRequest request, @RequestBody FeedbackEntity feedback) {
    	Application.getLogger().info("Endpoint called: [Rating:" + feedback.getRating() 
    									+ ", Text:" + feedback.getText() + "]");

        Tuple<String, HttpStatus> responseTuple = validateFeedback(feedback);
    	
    	if(!responseTuple.getV().equals(HttpStatus.OK))
    	{
            return new ResponseEntity<String>(responseTuple.getK(), responseTuple.getV());
        }

        //TODO - Use this in database storage and also log the data storage
        UUID uniqueId = UUID.randomUUID();
        feedback.setId(uniqueId.toString());
        feedbackDb.save(feedback);
        String linkUrl = String.format(
                "%s://%s:%d/feedback/%s", request.getScheme(), request.getServerName(),
                request.getServerPort(), uniqueId.toString()
        );
        Tuple<String, HttpStatus> fullOkResponseTuple = new Tuple<String, HttpStatus>(
                String.format(full_response_template, responseTuple.getK(), linkUrl),
                responseTuple.getV()
        );
        return new ResponseEntity<String>(fullOkResponseTuple.getK(), fullOkResponseTuple.getV());
    }
    
    protected Tuple<String, HttpStatus> validateFeedback(FeedbackEntity feedback) {
        // TODO: Need a better way to seperate validation (error-handling) from making results
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

    @GetMapping("/{id}")
    public ResponseEntity<String> getFeedback(@PathVariable("id") String id) {
        Tuple<String, HttpStatus> responseTuple = getFeedbackFromDB(id);
        return new ResponseEntity<String>(responseTuple.getK(),responseTuple.getV());
    }

    protected Tuple<String, HttpStatus> getFeedbackFromDB(String id) {
        List<FeedbackEntity> dbResponse = feedbackDb.findById(id);
        if(dbResponse.isEmpty()) {
            Application.getLogger().info("There was no feedback with that id");
            return new Tuple<String, HttpStatus>("No entry with that id\n", HttpStatus.NOT_FOUND);
        } else {
            FeedbackEntity response =  dbResponse.get(0);
            Application.getLogger().info("Entry being returned : " + response.toString());
            return new Tuple<String, HttpStatus>(response.toString(), HttpStatus.OK);
        }
    }
	
}
