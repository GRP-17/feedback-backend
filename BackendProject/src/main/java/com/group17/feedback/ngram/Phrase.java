package com.group17.feedback.ngram;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "phrases", schema = "hy1xosk6o5taszzw")
public class Phrase {
    @Id
	@NotNull(message = "Needs a ngram phrase")
	@Column(name = "ngram", columnDefinition = "VARCHAR(36)")
	private String ngram;
	
	@NotNull(message = "Needs a volume count")
	@Column(name = "negativeVolume", columnDefinition = "INT(11)")
	private Integer negativeVolume;
	
	public Phrase(String ngram, int negativeVolume) {
		this.ngram = ngram;
		this.negativeVolume = negativeVolume;
	}
	
	public Phrase() {
		this("unknown", 0);
	}
	
	public String getNgram() {
		return ngram;
	}
	
	public void setNgram(String ngram) {
		this.ngram = ngram;
	}
	
	public int getNegativeVolume() {
		return negativeVolume;
	}
	
	public void setNegativeVolume(int volume) {
		this.negativeVolume = volume;
	}
	
	
}
