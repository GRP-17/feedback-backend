package com.group17;

public class FeedbackNotFoundException extends RuntimeException {

    FeedbackNotFoundException(String id) {
        super("Could not find feedback " + id);
    }
}
