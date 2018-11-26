package com.group17;

import com.group17.utils.CommonException;
import com.group17.utils.ErrorResponse;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/feedback", produces = "application/hal+json")
public class FeedbackController {

  private final FeedbackRepository repository;

  private final FeedbackResourceAssembler assembler;

  FeedbackController(FeedbackRepository repository, FeedbackResourceAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping()
  Resources<Resource<Feedback>> findAll() {

    List<Resource<Feedback>> feedback =
        repository.findAll().stream().map(assembler::toResource).collect(Collectors.toList());

    Application.getLogger().info("[feedback/browse] Found. Object list size: " + feedback.size());

    return new Resources<>(
        feedback, linkTo(methodOn(FeedbackController.class).findAll()).withSelfRel());
  }

  @PostMapping(headers = "Accept=application/json")
  ResponseEntity<?> create(@RequestBody Feedback newFeedback)
      throws URISyntaxException, TransactionSystemException {

    Resource<Feedback> resource = assembler.toResource(repository.save(newFeedback));

    Application.getLogger()
        .info(
            "[feedback/create] Created: "
                + newFeedback.getId()
                + ". Object: "
                + newFeedback.toString());

    return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
  }

  @GetMapping("/{id}")
  Resource<Feedback> findOne(@PathVariable String id) throws CommonException {

    Feedback feedback =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new CommonException(
                        "Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));

    Application.getLogger()
        .info("[feedback/retrieve] Retrieved: " + id + ". Object: " + feedback.toString());

    return assembler.toResource(feedback);
  }

  @PutMapping("/{id}")
  ResponseEntity<?> update(@RequestBody Feedback newFeedback, @PathVariable String id)
      throws URISyntaxException, TransactionSystemException {

    Feedback updatedFeedback =
        repository
            .findById(id)
            .map(
                feedback -> {
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
            .orElseThrow(
                () ->
                    new CommonException(
                        "Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));

    Application.getLogger()
        .info("[feedback/update] Updated: " + id + ". Object: " + newFeedback.toString());

    Resource<Feedback> resource = assembler.toResource(updatedFeedback);

    return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> delete(@PathVariable String id) {

    try {
      repository.deleteById(id);
      Application.getLogger().info("[feedback/delete] Deleted: " + id);
    } catch (Exception e) {
      throw new CommonException("Could not find feedback: " + id, HttpStatus.NOT_FOUND.value());
    }

    return ResponseEntity.noContent().build();
  }

  /* Error Handlers */
  // TODO: could be extended from a common controller class
  @ExceptionHandler(CommonException.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(CommonException ex) {
    Application.getLogger().info("[exception] " + ex.getMessage());

    ErrorResponse error = new ErrorResponse();
    error.setErrorCode(ex.getErrorCode());
    error.setMessage(ex.getMessage());

    return new ResponseEntity<>(error, HttpStatus.OK);
  }

  @ExceptionHandler(TransactionSystemException.class)
  public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(
      TransactionSystemException ex) {

    // loop until find the ConstraintViolationException
    Throwable t = ex;
    while ((t != null) && !(t instanceof ConstraintViolationException)) {
      t = t.getCause();
    }

    // loop the list to get results
    StringBuilder msgList = new StringBuilder();
    for (ConstraintViolation<?> constraintViolation :
        ((ConstraintViolationException) t).getConstraintViolations()) {
      msgList.append(constraintViolation.getMessage());
    }
    ErrorResponse error = new ErrorResponse();
    error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
    error.setMessage(msgList.toString());

    return new ResponseEntity<>(error, HttpStatus.OK);
  }
}
