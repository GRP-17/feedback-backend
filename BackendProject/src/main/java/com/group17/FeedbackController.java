package com.group17;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/feedback", produces = "application/hal+json")
public class FeedbackController {
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

    @GetMapping("/{id}")
    Resource<Feedback> findOne(@PathVariable String id) {

        Feedback feedback = repository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));

        return assembler.toResource(feedback);
    }

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
