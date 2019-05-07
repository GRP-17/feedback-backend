package com.group17.phrase.blacklist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.group17.dashboard.Dashboard;

@Entity
@Table(name = "blacklisted_phrase", schema = "hy1xosk6o5taszzw")
public class BlacklistedPhrase {
	/**
	 * The id of the phrase, auto generated using the UUID generator (below).
	 * <p>
	 * It is always 36 characters long.
	 */
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "VARCHAR(36)")
	private String phraseId;

	/**
	 * The {@link Dashboard} this feedback belongs to.
	 */
	@NotNull(message = "Invalid dashboardId")
	@Column(columnDefinition = "VARCHAR(36)")
	private String dashboardId;
	
	/**
	 * The optional comment that can be left with the feedback. Hence not required in valid feedback.
	 * Shouldn't be way to long, as there is a limit on how much the database can store in one text cell.
	 * maps onto the text column.
	 */
	@NotNull(message = "Invalid phrase")
	@Size(max = 65535)
	@Column(columnDefinition = "text")
	private String phrase;
	
	/**
	 * Constructor.
	 */
	public BlacklistedPhrase() {
	}

	/**
	 * constructor for the general mapping of the JSON body onto the feedback object
	 *
	 * @param dashboardId
	 * @param phrase   the comment left with the feedback
	 */
	public BlacklistedPhrase(String dashboardId, String phrase) {
		this.dashboardId = dashboardId;
		this.phrase = phrase;
	}

	public String getId() {
		return phraseId;
	}

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}
	
	public String getPhrase() {
		return phrase;
	}
	
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

}
