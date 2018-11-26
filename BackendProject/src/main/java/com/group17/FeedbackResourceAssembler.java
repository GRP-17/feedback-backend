package com.group17;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class FeedbackResourceAssembler implements ResourceAssembler<Feedback, Resource<Feedback>> {

  @Override
  public Resource<Feedback> toResource(Feedback feedback) {
    return new Resource<>(
        feedback,
        linkTo(methodOn(FeedbackController.class).findOne(feedback.getId())).withSelfRel(),
        linkTo(methodOn(FeedbackController.class).findAll()).withRel("feedback"));
  }
}
