package com.group17.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Used to represent the database using Jpa
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String> {}
