package com.group17.phrase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.group17.feedback.Feedback;
import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.FiltersBuilder;
import com.group17.feedback.filter.query.Queries;
import com.group17.phrase.blacklist.BlacklistedPhrase;
import com.group17.phrase.blacklist.BlacklistedPhraseRepository;
import com.group17.phrase.blacklist.BlacklistedPhraseResourceAssembler;
import com.group17.phrase.termvector.TermVector;
import com.group17.util.DateUtil;
import com.group17.util.LoggerUtil;
import com.group17.util.exception.CommonException;

/**
 * Handles anything phrase-related; calling the {@link SearchboxGateway}
 * when necessary.
 */
@Service
public class PhraseService {
	/**
	 * Holds the JpaContext instance for entity managers.
	 */
	@Autowired private JpaContext jpaContext;
	/** Holds the instance of the BlacklistedPhraseRepository which represents the database. */
	@Autowired private BlacklistedPhraseRepository repository;
	/** Holds the instance of the factory which will make the resources. */
	@Autowired private BlacklistedPhraseResourceAssembler assembler;
	
	@Autowired private SearchboxGateway gateway;
	
	/**
	 * Register a {@link Feedback} object being created with Searchbox.
	 * <p>
	 * This will add the text of the {@link Feedback} object to the
	 * Searchbox database for future analysis.
	 * 
	 * @param feedback the {@link Feedback} to add
	 * @return whether it was successful
	 */
	public boolean onFeedbackCreated(Feedback feedback) {
		return gateway.put(feedback.getId(), feedback.getText());
	}
	
	/**
	 * Register a {@link Feedback} object being removed from Searchbox.
	 * <p>
	 * This will remove the text of the {@link Feedback} object from the
	 * Searchbox database.
	 * 
	 * @param feedback the {@link Feedback} to remove
	 * @return whether it was successful
	 */
	public boolean onFeedbackRemoved(String id) {
		return gateway.delete(id);
	}
	
	/**
	 * Get the common phrases in the (Searchbox) database.
	 *
	 * @param filters the {@link Filter}s to apply
	 * @param amount  the maximum number of common phrases to retrieve
	 * @return the common phrases against their term vectors
	 */
	public List<TermVector> getCommonPhrases(Filters filters, int amount) {
		// Add a filter based on the last month, however if they specify a date to find
		// it by already, then we won't merge
		filters = filters.clone().merge(FiltersBuilder
						.newInstance()
						.age(DateUtil.getLastMonth())
						.build(),
				false);

		// Get the IDs of the Feedback we're going to send to Elasticsearch using queries
		Query query = Queries.FEEDBACK_IDS.build(getFEntityManager(), filters);
		List<String> ids = new ArrayList<String>();
		for (Object obj : query.getResultList()) {
			ids.add((String) obj);
		}

		List<TermVector> toReturn = new ArrayList<TermVector>();
		if (!ids.isEmpty()) {
			Map<String, TermVector> phrases = getCommonPhrases(ids);
			if (phrases != null) {
				LoggerUtil.log(Level.INFO, "Size before filtering : " + phrases.size());

				// filter out single word phrases (by searching for a space in the term)
				List<TermVector> filteredPhrases = new ArrayList<TermVector>();
				for (Map.Entry<String, TermVector> entry : phrases.entrySet()) {
					if (!entry.getValue().getTerm().contains(" "))
						continue; // Filter out any 1-grams
					
					filteredPhrases.add(entry.getValue());
				}
				LoggerUtil.log(Level.INFO, "Size after filtering : " + filteredPhrases.size());

				// sort based on score - get TermVectors with highest score
				Collections.sort(filteredPhrases);
				Iterator<TermVector> iterator = filteredPhrases.iterator();

				int target = Math.min(filteredPhrases.size(), amount);
				int counter = 0;
				while(iterator.hasNext() && counter < target) {
					TermVector vector = iterator.next();
					if(isBlacklisted(vector)) continue;
					
					toReturn.add(vector);
					counter ++;
				}
			}

			// Sort the TermVectors so the highest frequency is first
			toReturn.sort(new Comparator<TermVector>() {

				@Override
				public int compare(TermVector o1, TermVector o2) {
					return -o1.getFrequency().compareTo(o2.getFrequency());
				}

			});
		}
		return toReturn;
	}
  
	/**
	 * Using the {@link SearchboxGateway}, make a request to the mtermvectors
	 * endpoint and return the terms along with their frequencies for specified
	 * {@link Feedback ids}.
	 * 
	 * @param ids the {@link Feedback} ids to get the {@link TermVector}s of
	 * @return a map of terms against their frequency
	 */
	public Map<String, TermVector> getCommonPhrases(Collection<String> ids) {

		// Create fields for JSON
		ArrayList<String> fields = new ArrayList<>();
		fields.add("text_field");

		// Get the term vectors
		Map<String, TermVector> result = gateway.getMTermVectors(ids, fields);
		return result;
	}

	public List<Resource<BlacklistedPhrase>> getBlacklistedPhrasesByDashboardId(String dashboardId) {
		return repository.findByDashboardId(dashboardId)
										.stream()
										.map(assembler::toResource)
										.collect(Collectors.toList());
	}
	
	public BlacklistedPhrase saveBlacklistedPhrase(BlacklistedPhrase phrase) {
		return repository.save(phrase);
	}

	public Resource<BlacklistedPhrase> createBlacklistedPhrase(
											BlacklistedPhrase newBlacklistedPhrase) {
		
		return assembler.toResource(saveBlacklistedPhrase(newBlacklistedPhrase));
	}

	public Resource<BlacklistedPhrase> getBlacklistedPhraseById(String phraseId) {
		BlacklistedPhrase label = repository.findById(phraseId)
				.orElseThrow(() -> new CommonException("Could not find blacklisted phrase: " 
															+ phraseId,
													   HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(label);
	}

	public Resource<BlacklistedPhrase> updateBlacklistedPhrase(String phraseId,
											BlacklistedPhrase newBlacklistedPhrase) {

		BlacklistedPhrase updatedPhrase = repository.findById(phraseId).map(phrase -> {
			if(newBlacklistedPhrase.getDashboardId() != null) {
				phrase.setDashboardId(newBlacklistedPhrase.getDashboardId());
			}
			if(newBlacklistedPhrase.getPhrase() != null) {
				phrase.setPhrase(newBlacklistedPhrase.getPhrase());
			}
			return saveBlacklistedPhrase(phrase);
		}).orElseThrow(() -> new CommonException("Could not find blacklisted phrase: " 
															+ phraseId,
												 HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(updatedPhrase);
	}

	public void deleteBlacklistedPhraseById(String phraseId) {
		repository.deleteById(phraseId);
	}
	
	private boolean isBlacklisted(TermVector term) {
		String phrase = term.getTerm().toLowerCase();
		return !repository.findByPhrase(phrase).isEmpty();
	}
	
	/**
	 * Get the {@link javax.persistence.EntityManager} for the {@link Feedback} entity.
	 *
	 * @return the {@link javax.persistence.EntityManager}
	 */
	private EntityManager getFEntityManager() {
		return jpaContext.getEntityManagerByManagedType(Feedback.class);
	}
	
	public SearchboxGateway getSearchboxGateway() {
		return gateway;
	}
	
	public void setSearchboxGateway(SearchboxGateway gateway) {
		this.gateway = gateway;
	}
  
}
