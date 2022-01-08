package com.app.Kmail.model.view;

public class InboxViewModel {
    Long id;
    String from;
    String date;
    String title;
    boolean isRead;

    public Long getId() {
        return id;
    }

    public InboxViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public InboxViewModel setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getDate() {
        return date;
    }

    public InboxViewModel setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public InboxViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isRead() {
        return isRead;
    }

    public InboxViewModel setRead(boolean read) {
        isRead = read;
        return this;
    }
}
