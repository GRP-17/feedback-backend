package com.group17;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "feedback", schema = "hy1xosk6o5taszzw")
public class Feedback {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;
    private Integer rating;
    private String text;

    public Feedback() {}

    public Feedback(Integer rating, String text) {
		this.rating = rating;
		this.text = text;
	}

	@Column(name = "id")
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    @Column(name = "rating")
	public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) { this.rating = rating; }

    public String getText() {
        return text;
    }

    @Column(name = "text")
    public void setText(String text) {
        this.text = text;
    }

    public String getStars() {
        String stars = "";
        for(int i=0; i < rating; i++) 
            stars += "*";
        return stars;
    }

    @Override
    public String toString() {
        return String.format("Feedback [id=%s, rating=%s, text=%s]\n", id, rating, text);
    }
}
