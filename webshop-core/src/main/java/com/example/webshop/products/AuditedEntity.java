package com.example.webshop.products;

import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public class AuditedEntity {
    
    @Id
    public String id;

    public long createdAt;
    public long updatedAt;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString();
        createdAt = updatedAt = System.currentTimeMillis();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = System.currentTimeMillis();
    }
}
