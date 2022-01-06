package com.app.Kmail.model.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
public class EmailEntity extends BaseEntity {

    @ManyToOne
    private UserEntity from;
    @ManyToOne
    private UserEntity to;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Lob
    private File attachment;
    @Column(nullable = false)
    private boolean isRead;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    public boolean isRead() {
        return isRead;
    }

    public EmailEntity setRead(boolean read) {
        isRead = read;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public EmailEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }


    public UserEntity getFrom() {
        return from;
    }

    public EmailEntity setFrom(UserEntity from) {
        this.from = from;
        return this;
    }

    public UserEntity getTo() {
        return to;
    }

    public EmailEntity setTo(UserEntity to) {
        this.to = to;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EmailEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EmailEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public File getAttachment() {
        return attachment;
    }

    public EmailEntity setAttachment(File attachment) {
        this.attachment = attachment;
        return this;
    }

}
