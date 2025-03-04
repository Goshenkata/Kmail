package com.app.Kmail.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class SchemaVersionEntity extends BaseEntity{
    @Column(nullable = false, unique = true)
    private Integer version;
    @Column(nullable = false)
    private LocalDateTime created;

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
