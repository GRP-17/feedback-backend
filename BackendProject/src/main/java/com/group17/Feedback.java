package com.group17;

public class Feedback {
    private final Integer rating;
    private final String text;

    public Feedback(Integer rating, String text) {
		this.rating = rating;
		this.text = text;
	}

	public Integer getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public String getStars() {
        String stars = "";
        for(int i=0; i < rating; i++) 
            stars += "*";
        return stars;
    }
}