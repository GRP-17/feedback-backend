package com.group17.feedback;

import static com.group17.util.Constants.FEEDBACK_MAX_RATING;
import static com.group17.util.Constants.FEEDBACK_MIN_RATING;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import com.group17.dashboard.Dashboard;
import com.group17.feedback.tone.Sentiment;
import com.group17.label.Label;

/**
 * The Java Object; defining the schema, of the table we want to map to.
 */
@Entity
@Table(name = "feedback", schema = "hy1xosk6o5taszzw")
public class Feedback {
	/**
	 * The id of the feedback, auto generated using the UUID generator (below).
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
	 * When this feedback was created.
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, columnDefinition = "TIMESTAMP")
    private Date created;

	/**
	 * The rating of the feedback. Is required to be present in valid feedback.
	 * should be 1 - 5 (as its representing the stars given).
	 * maps onto the rating column.
	 */
	@NotNull(message = "Bad Feedback Format")
	@Range(min = FEEDBACK_MIN_RATING, max = FEEDBACK_MAX_RATING, 
		   message = "The range of rating is {min} ~ {max}.")
	@Column(name = "rating", columnDefinition = "INT(11)")
	private Integer rating;

	/**
	 * The optional comment that can be left with the feedback. Hence not required in valid feedback.
	 * Shouldn't be way to long, as there is a limit on how much the database can store in one text cell.
	 * maps onto the text column.
	 */
	@Size(max = 65535)
	@Column(name = "text", columnDefinition = "text")
	private String text;

	/**
	 * The sentiment of the {@link #text}. This will be set to the
	 * value of {@link Sentiment#toString()}.
	 * <p>
	 * If there is no {@link #text}, then this will default to
	 * {@link Sentiment#NEUTRAL}.
	 * 
	 */
	@Size(max = 65535)
	@Column(name = "sentiment")
	private String sentiment;
	
	@Column(name="pinned")
	private boolean pinned;
	
	/**
	 * This is specifying a table to be needed, where it is a composite primary key of the
	 * feedback id & label name.
	 */
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "feedback_label",
        joinColumns =  @JoinColumn(name = "feedback_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
	private Set<Label> labels;
	
	/**
	 * the default constructor
	 * sets the text to a default value of nothing if it's not included in the request JSON body
	 */
	public Feedback() {
		text = "";
	}

	/**
	 * constructor for the general mapping of the JSON body onto the feedback object
	 * @param rating the rating of the feedback
	 * @param text the comment left with the feedback
	 */
	public Feedback(String dashboardId, Integer rating, String text, Label... labels) {
		this.dashboardId = dashboardId;
		this.rating = rating;
		this.text = text;
		this.pinned = false;
		
		this.labels = Stream.of(labels).collect(Collectors.toSet());
		this.labels.forEach(x -> x.getFeedback().add(this));
	}
	
	@PrePersist
	protected void onCreate() {
		created = new Date();
	}
	
	public String getId() {
		return id;
	}
	
	public String getDashboardId() {
		return dashboardId;
	}
	
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getSentiment() {
		return sentiment;
	}
	
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
	
	public Sentiment getSentimentEnum() {
		return Sentiment.valueOf(sentiment.toUpperCase());
	}
	
	public void setSentiment(Sentiment sentiment) {
		setSentiment(sentiment.toString());
	}
	
	public boolean isPinned() {
		return pinned;
	}
	
	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}
	
	public Set<Label> getLabels() {
		return labels;
	}
	
}
