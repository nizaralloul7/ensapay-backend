package com.ensa.ENSAPAY.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "firstname")),
        @AttributeOverride(name = "lastName", column = @Column(name="lastname")),
        @AttributeOverride(name = "username", column = @Column(nullable = false,unique = true)),
        @AttributeOverride(name = "password", column = @Column(nullable = false)),
        @AttributeOverride(name = "role", column = @Column),
        @AttributeOverride(name = "createdAt", column = @Column),
        @AttributeOverride(name = "updatedAt", column = @Column)
})
@Data
@MappedSuperclass
public abstract class ClientTemplate extends User
{
    private String email;
    @Column(nullable = false ,unique = true)
    private String phone;
    private String accountType;
}
