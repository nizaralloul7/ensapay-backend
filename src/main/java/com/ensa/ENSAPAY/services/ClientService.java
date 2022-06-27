package com.ensa.ENSAPAY.services;

import com.ensa.ENSAPAY.entities.*;
import com.ensa.ENSAPAY.repositories.BillRepository;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.repositories.DemandeRepository;
import com.ensa.ENSAPAY.security.auth.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService
{
    private final ClientRepository clientRepository;
    private final DemandeRepository demandeRepository;
    private final PasswordEncoder passwordEncoder;
    private final BillRepository billRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, DemandeRepository demandeRepository, PasswordEncoder passwordEncoder, BillRepository billRepository)
    {
        this.clientRepository = clientRepository;
        this.demandeRepository = demandeRepository;
        this.passwordEncoder = passwordEncoder;
        this.billRepository = billRepository;
    }

    public void changePassword(String newPassword)
    {
        String currentLoggedInUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(currentLoggedInUser).get();


        if(client.isPasswordChanged())
        {
            throw new IllegalStateException("Password already changed");
        }
        System.out.println(newPassword);
        clientRepository.changePasswordInFirstLogin(passwordEncoder.encode(newPassword), true, client.getId());
        System.out.println("Password changed successfully");
    }

    public List<Client> getClients()
    {
        // thank u nizar 1
        return this.clientRepository.findAll();
    }



    public List<Bill> getPaidBills()
    {
        String appUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(appUser).get();

        return billRepository.findPaidBills(BillState.paid.ordinal(),client.getId()).get();
    }

    public void createClientDemand(Client client)
    {

    }

    public void addClientRequest(Demande demande) {
        demande.setPhone("+212"+demande.getPhone());
        demande.setUsername(demande.getPhone());
        this.demandeRepository.save(demande);
    }

    public Client getMyInfos()
    {
        String appUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return clientRepository.findClientByUsername(appUser).get();
    }

    public List<Bill> getHistory()
    {
        String appUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client currentUser = clientRepository.findClientByUsername(appUser).get();

        return billRepository.findPaidBills(BillState.paid.ordinal(), currentUser.getId()).get();

    }
}
