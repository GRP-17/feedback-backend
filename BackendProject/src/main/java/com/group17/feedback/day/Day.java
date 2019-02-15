package com.group17.feedback.day;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.group17.util.DateUtil;

@Entity
@Table(name = "days", schema = "hy1xosk6o5taszzw")
public class Day {
	@Id
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date date;
	
	@NotNull(message = "Needs a locale date")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String timestamp;
	
	@NotNull(message = "Needs a negative sentiment count")
	@Column(name = "negativeCount", columnDefinition = "INT(11)")
	private Integer negativeCount;
	
	public Day(long date, int negativeCount) {
		this.date = new Date(date);
		this.timestamp = DateUtil.format(this.date.getTime());
		this.negativeCount = negativeCount;
	}
	
	public Day() {
		this(DateUtil.getToday(), 0);
	}
	
	public String getId() {
		return timestamp;
	}
	
	public long getDateTimestamp() {
		return date.getTime();
	}
	
	public int getNegativeSentimentCount() {
		return negativeCount;
	}
	
}
