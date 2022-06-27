package com.ensa.ENSAPAY.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "firstname")),
        @AttributeOverride(name = "lastName", column = @Column(name = "lastname")),
        @AttributeOverride(name = "username", column = @Column(nullable = false)),
        @AttributeOverride(name = "password", column = @Column(nullable = false)),
        @AttributeOverride(name = "role", column = @Column),
        @AttributeOverride(name = "createdAt", column = @Column),
        @AttributeOverride(name = "updatedAt", column = @Column)
})
public class Admin extends User
{
/*    @OneToMany
    private List<Agent> agentList;*/
}