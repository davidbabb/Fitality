package com.techelevator.model;

public class Motivational {

    private String author;
    private String quote;

    public Motivational (String author, String quote) {
        this.author = author;
        this.quote = quote;
    }

    public Motivational () {}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
