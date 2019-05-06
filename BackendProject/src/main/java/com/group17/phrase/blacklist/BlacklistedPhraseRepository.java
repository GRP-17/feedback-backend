package com.group17.phrase.blacklist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Represents the {@link BlacklistedPhrase} database using JPA.
 */
public interface BlacklistedPhraseRepository extends JpaRepository<BlacklistedPhrase, String> {
	
	List<BlacklistedPhrase> findByDashboardId(String dashboardId);
	
	List<BlacklistedPhrase> findByPhrase(String phrase);
	
}
