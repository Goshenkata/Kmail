package com.app.Kmail.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "emails")
public class EmailEntity extends BaseEntity {

    @ManyToOne
    private UserEntity from;
    @ManyToOne()
    private UserEntity to;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column()
    private String attachment;
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

    public String getAttachment() {
        return attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailEntity)) return false;
        EmailEntity email = (EmailEntity) o;
        return isRead == email.isRead && from.equals(email.from) && to.equals(email.to) && title.equals(email.title) && content.equals(email.content) && attachment.equals(email.attachment) && created.equals(email.created);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(from, to, title, content, isRead, created);
        result = 31 * result + attachment.hashCode();
        return result;
    }

    public EmailEntity setAttachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    public boolean isNotRead() {
        return !isRead;
    }
}
