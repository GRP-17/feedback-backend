package com.group17.feedback.day;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "day"/*, schema = "hy1xosk6o5taszzw"*/)
public class Day {
	@Id
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;
	
	@NotNull(message = "Needs a negative sentiment count")
	@Column(name = "negativeCount", columnDefinition = "INT(11)")
	private Integer negativeCount;
	
	public Day(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public int getNegativeSentimentCount() {
		return negativeCount;
	}
	
}
