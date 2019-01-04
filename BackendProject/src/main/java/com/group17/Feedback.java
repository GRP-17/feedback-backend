package com.group17;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

/**
 * defines the schema, of the table we want to map to, as a Java object
 */
@Entity
@Table(name = "feedback", schema = "hy1xosk6o5taszzw")
public class Feedback {
	/**
	 * The id of the feedback, auto generated using the UUID generator (below).
	 * Hence it is always 36 characters long.
	 * Maps onto the id column.
	 * */
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;

	/**
	 * When this feedback was created.
	 * <p>
	 * This is set as soon as the controller creates the resource.
	 */
	@Column(name = "dateCreated", columnDefinition = "dateCreated")
	private Date dateCreated;

	/**
	 * The rating of the feedback. Is required to be present in valid feedback.
	 * should be 1 - 5 (as its representing the stars given).
	 * maps onto the rating column.
	 */
	@NotNull(message = "Rating is required.")
	@Range(min = 1, max = 5, message = "The range of rating is {min} ~ {max}.")
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
	@Column(name = "sentiment", columnDefinition = "sentiment")
	private String sentiment;

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
	public Feedback(Integer rating, String text) {
		this.rating = rating;
		this.text = text;
	}

	public String getId() {
		return id;
	}
	
	public void setDateCreated(Date date) {
		this.dateCreated = date;
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
	
	public void setSentiment(Sentiment sentiment) {
		setSentiment(sentiment.toString());
	}

	/**
	 * @return the rating as a number of stars
	 */
	public String getStars() {
		String stars = "";
		for (int i = 0; i < rating; i++) stars += "*";
		return stars;
	}

	/**
	 * @return a formatted string containing all the information about this feedback
	 */
	@Override
	public String toString() {
		return String.format("Feedback [id=%s, rating=%s, text=%s, sentiment=%s]\n", 
							 id, rating, text, sentiment);
	}
}
