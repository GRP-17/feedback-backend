package com.group17;

public class Feedback {

    private Integer rating;
    private String text;

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
        for(int i=0; i < rating; i++) 
            stars += "*";
        return stars;
    }
}