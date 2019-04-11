package com.group17.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the {@link Dashboard} database using JPA.
 */
public interface DashboardRepository extends JpaRepository<Dashboard, String> {}
