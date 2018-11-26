package com.group17;

import org.springframework.data.jpa.repository.JpaRepository;

interface FeedbackRepository extends JpaRepository<Feedback, String> {}
