package com.ensa.ENSAPAY.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Table
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "email", column = @Column),
        @AttributeOverride(name = "phone", column = @Column(unique = true)),
        @AttributeOverride(name = "accountType", column = @Column)
})
@Data
public class Client extends ClientTemplate
{
    @OneToMany
    private List<Bill> clientBill;
    @ManyToOne
    @JoinColumn(name="created_by")
    private Agent createdBy;

    private BigDecimal balance = new BigDecimal(0);
    public Client(){

    }
    public Client(Demande demande)
    {
        this.setFirstName(demande.getFirstName());
        this.setLastName(demande.getLastName());
        this.setUsername(demande.getUsername());
        this.setEmail(demande.getEmail());
        this.setPhone(demande.getPhone());
        this.setAccountType(demande.getAccountType());
        this.setCreatedAt(demande.getCreatedAt());
    }
}