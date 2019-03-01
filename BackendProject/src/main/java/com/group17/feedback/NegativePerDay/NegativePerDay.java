package com.group17.feedback.NegativePerDay;

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

	public void setVolume(int count){
		this.volume = count;
	}

	public int getVolume() {
		return volume;
	}

	public void increaseVolume(int increment) {
		volume += increment;
	}

	public void increaseVolume() {
		increaseVolume(1);
	}
}
