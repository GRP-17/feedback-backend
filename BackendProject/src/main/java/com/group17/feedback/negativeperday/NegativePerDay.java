package com.group17.feedback.negativeperday;

import com.group17.util.DateUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "negative_per_day", schema = "hy1xosk6o5taszzw")
public class NegativePerDay {
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
	private Date date;

	@NotNull(message = "Needs a negative sentiment count")
	@Column(name = "volume", columnDefinition = "INT(11)")
	private Integer volume;

	public NegativePerDay(Date date, int volume) {
		this.date = DateUtil.getDayStart(date);
		this.volume = volume;
	}

	public NegativePerDay(Date date) {
		this(DateUtil.getDayStart(date), 1);
	}

	public NegativePerDay() {
		this(DateUtil.getTodayStart(), 1);
	}

	public Date getId() {
		return date;
	}

	public Date getDate() {
		return date;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int count) {
		this.volume = count;
	}

	public void increaseVolume(int increment) {
		volume += increment;
	}

	public void increaseVolume() {
		increaseVolume(1);
	}
}
