package com.group17;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "feedback", schema = "hy1xosk6o5taszzw")
public class Feedback {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;

	@NotNull(message = "Rating is required.")
	@Range(min = 1, max = 10, message = "The range of rating is {min} ~ {max}.")
	@Column(name = "rating", columnDefinition = "INT(11)")
	private Integer rating;

	@Size(min = 1, max = 65535)
	@Column(name = "text", columnDefinition = "text")
	private String text;

	public Feedback() {}

	public Feedback(Integer rating, String text) {
		this.rating = rating;
		this.text = text;
	}

	public String getId() {
		return id;
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

	public String getStars() {
		String stars = "";
		for (int i = 0; i < rating; i++) stars += "*";
		return stars;
	}

	@Override
	public String toString() {
		return String.format("Feedback [id=%s, rating=%s, text=%s]\n", id, rating, text);
	}
}
