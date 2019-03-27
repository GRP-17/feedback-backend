package com.group17.feedback.filter.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.NotImplementedException;

import com.group17.feedback.filter.Filter;

public abstract class QueryBuilder {
	private final EntityManager entityManager;
	private Set<String> selectClauses;
	private Set<String> tables;
	private Map<String, String> whereClauses;
	
	protected QueryBuilder(EntityManager entityManager) {
		this.entityManager = entityManager;
		
		this.selectClauses = new HashSet<String>();
		this.tables = new HashSet<String>();
		this.whereClauses = new HashMap<String, String>();
	}
	
	public void applyFilter(Filter filter) {
		switch(filter.getType()) {
		case AGE:
			break;
		case SENTIMENT:
		case TEXT_CONTAINING:
		default:
			// TODO
			throw new NotImplementedException(new RuntimeException());
		}
	}
	
	// TODO:
	// public abstract QueryBuilder applyFilter(Filter filter);
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void addWhereClause(String key, String value) {
		whereClauses.put(key, value);
	}

}
