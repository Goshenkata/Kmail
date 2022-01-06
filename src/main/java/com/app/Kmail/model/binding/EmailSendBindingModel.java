package com.app.Kmail.model.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class EmailSendBindingModel {

    @NotNull
    private String from;
    @NotNull
    private String to;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private MultipartFile attachment;

    public EmailSendBindingModel setFrom(String from) {
        this.from = from;
        return this;
    }

    public EmailSendBindingModel setTo(String to) {
        this.to = to;
        return this;
    }

    public EmailSendBindingModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public EmailSendBindingModel setContent(String content) {
        this.content = content;
        return this;
    }

    public EmailSendBindingModel setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public EmailSendBindingModel() {
    }


    @Override
    public String toString() {
        return "EmailSendBindingModel{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", attachment=" + attachment +
                ", attachment=" + attachment +
                '}';
    }
}
