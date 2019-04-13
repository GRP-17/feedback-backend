package com.group17.negativeperday;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.group17.util.DateUtil;

/**
 * The Java Object; defining the schema, of the table we want to map to.
 */
@Entity
@Table(name = "negative_per_day", schema = "hy1xosk6o5taszzw")
public class NegativePerDay {
	/**
	 * The id of the negative per day, auto generated using the UUID generator (below).
	 * <p>
	 * It is always 36 characters long.
	 */
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;
	
	/**
	 * The {@link Dashboard} this feedback belongs to.
	 */
	@NotNull(message = "Invalid dashboardId")
	@Column(name = "dashboardId", columnDefinition = "VARCHAR(36)")
	private String dashboardId;
	
	/**
	 * When this negative per day was created / the date it represents.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
	private Date date;

	/**
	 * How many negative reviews were left on this {@link #date} for 
	 * this {@link #dashboardId}.
	 */
	@NotNull(message = "Needs a negative sentiment count")
	@Column(name = "volume", columnDefinition = "INT(11)")
	private Integer volume;

	public NegativePerDay(String dashboardId, Date date, int volume) {
		this.date = date;
		
		this.dashboardId = dashboardId;
		this.volume = volume;
	}

	public NegativePerDay() {
		this("", DateUtil.getTodayStart(), 1);
	}
	
	public String getDashboardId() {
		return dashboardId;
	}
	
	public String getId() {
		return id;
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
