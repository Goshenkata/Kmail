package com.app.Kmail.model.view;

public class EmailViewModel {
    String from;
    String date;
    String title;
    boolean isRead;

    public String getFrom() {
        return from;
    }

    public EmailViewModel setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getDate() {
        return date;
    }

    public EmailViewModel setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EmailViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isRead() {
        return isRead;
    }

    public EmailViewModel setRead(boolean read) {
        isRead = read;
        return this;
    }
}
