package com.group17;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FeedbackRepository extends CrudRepository<FeedbackEntity, Long> {

    List<FeedbackEntity> findByRating(Integer rating);
    List<FeedbackEntity> findById(String id);
}
