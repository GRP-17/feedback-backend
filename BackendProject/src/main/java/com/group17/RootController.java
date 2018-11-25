package com.group17;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RootController {

    @GetMapping
    ResourceSupport index() {
        ResourceSupport rootResource = new ResourceSupport();
        rootResource.add(linkTo(methodOn(FeedbackController.class).findAll()).withRel("feedback"));
        return rootResource;
    }
}