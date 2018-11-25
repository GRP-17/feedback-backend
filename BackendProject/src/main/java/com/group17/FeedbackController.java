package com.group17;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
//import java.util.UUID;
import java.util.stream.Collectors;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.group17.util.Tuple;

//import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/feedback", produces = "application/hal+json")
public class FeedbackController {
//    private final String response_template = "Feedback:\n-Rating: %s\n-Text: %s\n";
//    private final String full_response_template = "%s -Link: %s\n";

//    @Autowired
    private final FeedbackRepository repository;

    private final FeedbackResourceAssembler assembler;

    FeedbackController(FeedbackRepository repository,
                       FeedbackResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping()
    Resources<Resource<Feedback>> findAll() {

        List<Resource<Feedback>> feedback = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(feedback,
                linkTo(methodOn(FeedbackController.class).findAll()).withSelfRel());
    }

    @PostMapping(headers = "Accept=application/json")
    ResponseEntity<?> create(@RequestBody Feedback newFeedback) throws URISyntaxException {

        Resource<Feedback> resource = assembler.toResource(repository.save(newFeedback));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
//    public ResponseEntity<String> createFeedback(HttpServletRequest request, @RequestBody Feedback feedback) {
//    	Application.getLogger().info("Endpoint called: [Rating:" + feedback.getRating()
//    									+ ", Text:" + feedback.getText() + "]");
//
//        Tuple<String, HttpStatus> responseTuple = validateFeedback(feedback);
//
//    	if(!responseTuple.getV().equals(HttpStatus.OK))
//    	{
//            return new ResponseEntity<String>(responseTuple.getK(), responseTuple.getV());
//        }
//
//        //TODO - Use this in database storage and also log the data storage
//        UUID uniqueId = UUID.randomUUID();
//        feedback.setId(uniqueId.toString());
//        repository.save(feedback);
//        String linkUrl = String.format(
//                "%s://%s:%d/feedback/%s", request.getScheme(), request.getServerName(),
//                request.getServerPort(), uniqueId.toString()
//        );
//        Tuple<String, HttpStatus> fullOkResponseTuple = new Tuple<String, HttpStatus>(
//                String.format(full_response_template, responseTuple.getK(), linkUrl),
//                responseTuple.getV()
//        );
//        return new ResponseEntity<String>(fullOkResponseTuple.getK(), fullOkResponseTuple.getV());
//    }
//
//    protected Tuple<String, HttpStatus> validateFeedback(Feedback feedback) {
//        // TODO: Need a better way to seperate validation (error-handling) from making results
//        if(feedback.getRating() == null) {
//        	Application.getLogger().error("Didn't provide rating");
//        	return new Tuple<String, HttpStatus>("No rating provided\n", HttpStatus.BAD_REQUEST);
//        } else if(feedback.getRating() < 1 || feedback.getRating() > 10) {
//        	Application.getLogger().error("Rating wasn't between 1 and 10");
//        	return new Tuple<String, HttpStatus>("Rating wasn't between 1 and 10\n", HttpStatus.BAD_REQUEST);
//        } else {
//        	String toReturn = String.format(response_template, feedback.getStars(), feedback.getText());
//        	Application.getLogger().info("Successfully returned " + toReturn);
//            return new Tuple<String, HttpStatus>(toReturn, HttpStatus.OK);
//        }
//    }

    @GetMapping("/{id}")
    Resource<Feedback> findOne(@PathVariable String id) {

        Feedback employee = repository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));

        return assembler.toResource(employee);
    }
//    public ResponseEntity<String> getFeedback(@PathVariable("id") String id) {
//        Tuple<String, HttpStatus> responseTuple = getFeedbackFromDB(id);
//        return new ResponseEntity<String>(responseTuple.getK(),responseTuple.getV());
//    }
//
//    protected Tuple<String, HttpStatus> getFeedbackFromDB(String id) {
//        List<Feedback> dbResponse = repository.findById(id);
//        if(dbResponse.isEmpty()) {
//            Application.getLogger().info("There was no feedback with that id");
//            return new Tuple<String, HttpStatus>("No entry with that id\n", HttpStatus.NOT_FOUND);
//        } else {
//            Feedback response =  dbResponse.get(0);
//            Application.getLogger().info("Entry being returned : " + response.toString());
//            return new Tuple<String, HttpStatus>(response.toString(), HttpStatus.OK);
//        }
//    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@RequestBody Feedback newFeedback, @PathVariable String id) throws URISyntaxException {

        Feedback updatedFeedback = repository.findById(id)
                .map(feedback -> {
                    Integer newRating = newFeedback.getRating();
                    String newText = newFeedback.getText();
                    if (newRating != null) {
                        feedback.setRating(newRating);
                    }
                    if (newText != null) {
                        feedback.setText(newText);
                    }
                    return repository.save(feedback);
                })
                .orElseThrow(() -> new FeedbackNotFoundException(id));

        Resource<Feedback> resource = assembler.toResource(updatedFeedback);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable String id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
