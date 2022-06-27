package com.ensa.ENSAPAY.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
@Entity
@Data
public class Unpaid
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne()
    @JoinColumn(name = "creditor_id", referencedColumnName = "id")
    private Creditor creditor;
    @OneToOne()
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    private String description;
    private UnpaidType type;
    private BigDecimal amount;
    private UnpaidState state = UnpaidState.PENDING;
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

