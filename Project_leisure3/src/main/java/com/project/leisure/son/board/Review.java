package com.project.leisure.son.board;

public class Review {
    private String name;
    private int rating;
    private String comment;

    public Review(String name, int rating, String comment) {
        this.name = name;
        this.rating = rating;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
