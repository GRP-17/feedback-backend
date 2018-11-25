package com.group17;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
class FeedbackNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(FeedbackNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String feedbackNotFoundHandler(FeedbackNotFoundException ex) {
        return ex.getMessage();
    }
}