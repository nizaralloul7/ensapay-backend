package com.ensa.ENSAPAY.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class User
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    // private IdentityCard identityCardType;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private boolean isPasswordChanged = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    public void setCreationDateTime() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setChangeDateTime() {
        this.updatedAt = LocalDateTime.now();
    }
}
