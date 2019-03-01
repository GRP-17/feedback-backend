package com.group17.feedback.ngram;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.group17.feedback.ngram.Phrase.PhraseId;

@Entity
@IdClass(PhraseId.class)
@Table(name = "phrases", schema = "hy1xosk6o5taszzw")
public class Phrase {
	@Id
	private Date date;
	@Id
	private String ngram;
	
	public Phrase(long date, String ngram) {
    	this.date = new Date(date);
    	this.ngram = ngram;
	}
	
	public Phrase() {
		this(System.currentTimeMillis(), "unknown");
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getNgram() {
		return ngram;
	}
	
	public class PhraseId implements Serializable {
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "date", nullable = false)
	    private Date date;

		@NotNull(message = "Needs an n-gram")
		@Column(name = "ngram", columnDefinition = "VARCHAR(36)")
		private String ngram;
	}
	
}
