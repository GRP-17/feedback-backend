package com.group17.label;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the {@link Label} database using JPA.
 */
public interface LabelRepository extends JpaRepository<Label, String> {}
