package com.group17;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Used to represent the database using Jpa
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String> {}
