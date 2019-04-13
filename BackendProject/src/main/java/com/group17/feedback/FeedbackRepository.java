package com.group17.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the {@link Feedback}  database using JPA.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String> {}
