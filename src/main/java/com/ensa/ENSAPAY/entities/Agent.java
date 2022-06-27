package com.ensa.ENSAPAY.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "firstname")),
        @AttributeOverride(name = "lastName", column = @Column(name = "lastname")),
        //@AttributeOverride(name = "birth", column = @Column),
       // @AttributeOverride(name = "address", column = @Column),
        //@AttributeOverride(name = "email", column = @Column),
       // @AttributeOverride(name = "phone", column = @Column(nullable = false)),
        @AttributeOverride(name = "username", column = @Column(nullable = false,unique = true)),
        @AttributeOverride(name = "password", column = @Column(nullable = false)),
        @AttributeOverride(name = "role", column = @Column),
        @AttributeOverride(name = "createdAt", column = @Column),
        @AttributeOverride(name = "updatedAt", column = @Column),
        @AttributeOverride(name="isPasswordChanged", column = @Column)
})
@Data
public class Agent extends User
{
 /*  @ManyToOne
   @JoinTable
   private Admin creator;

   @OneToMany
   @JoinTable
   private List<Client> clientsList;*/
   private String tradeRegister;
   private String patentNumber;
   private String address;
   private String email;
   private String phone;
   //identityCardType
   private IdentityType identityType;
   private String identityCardNumber;
   private String identityCardRecto;
   private String identityCardVerso;


}

