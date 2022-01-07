package com.app.Kmail.model.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

public class EmailServiceModel {

    private String from;
    private String to;
    private String title;
    private String content;
    private byte[] attachment;
    private boolean isRead;
    private LocalDateTime created;

    public boolean isRead() {
        return isRead;
    }

    public EmailServiceModel setRead(boolean read) {
        isRead = read;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public EmailServiceModel setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }


    public String getFrom() {
        return from;
    }

    public EmailServiceModel setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public EmailServiceModel setTo(String to) {
        this.to = to;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EmailServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EmailServiceModel setContent(String content) {
        this.content = content;
        return this;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public EmailServiceModel setAttachment(byte []attachment) {
        this.attachment = attachment;
        return this;
    }

    public EmailServiceModel() {
    }

}
