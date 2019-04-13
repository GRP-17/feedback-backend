package com.group17.negativeperday;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the {@link NegativePerDay}  database using JPA.
 */
public interface NegativePerDayRepository extends JpaRepository<NegativePerDay, Date> {}
