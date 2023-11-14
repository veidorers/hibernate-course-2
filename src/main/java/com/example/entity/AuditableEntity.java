package com.example.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {
    private Instant createdAt;
    private String createdBy;

    private Instant updatedAt;
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(Instant.now());
    }
}
