package com.app.Kmail.model.view;

public class LoggedInUserIndexViewModel {
    String fullName;

    public String getFullName() {
        return fullName;
    }

    public LoggedInUserIndexViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public LoggedInUserIndexViewModel setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
        return this;
    }

    int unreadMessages;
}
