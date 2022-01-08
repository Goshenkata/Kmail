package com.app.Kmail.model.view;

public class EmailViewModel {
    String from;
    String to;
    String title;
    String content;
    String date;
    String downloadFileLink;
    String attachmentName;
    boolean hasAttachment;

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public EmailViewModel setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public EmailViewModel setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public EmailViewModel setTo(String to) {
        this.to = to;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EmailViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EmailViewModel setContent(String content) {
        this.content = content;
        return this;
    }

    public String getDate() {
        return date;
    }

    public EmailViewModel setDate(String date) {
        this.date = date;
        return this;
    }

    public String getDownloadFileLink() {
        return downloadFileLink;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public EmailViewModel setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailViewModel setDownloadFileLink(String downloadFileLink) {
        this.downloadFileLink = downloadFileLink;
        return this;
    }
}
