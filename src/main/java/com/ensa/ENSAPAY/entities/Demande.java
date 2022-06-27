package com.ensa.ENSAPAY.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "password", column = @Column(nullable = true))
})
public class Demande extends ClientTemplate
{

}
