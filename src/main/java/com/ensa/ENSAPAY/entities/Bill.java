package com.ensa.ENSAPAY.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Table
@Entity
@Data
public class Bill
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "creditor_id", referencedColumnName = "id")
    private Creditor creditor;
    @OneToOne()
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany
    private List<Unpaid> unpaidList;
    private BigDecimal totalAmount;
    private BillState state;
    private String verificationCode;

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

