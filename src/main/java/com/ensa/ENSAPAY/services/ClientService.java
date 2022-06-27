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

    public boolean changePassword(String newPassword)
    {
        ApplicationUser currentLoggedInUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(currentLoggedInUser.getUsername()).get();

        if(!client.isPasswordChanged())
        {
            clientRepository.changePasswordInFirstLogin(passwordEncoder.encode(newPassword), true, client.getId());
            System.out.println("password changed successfully");
            return false;
        }
        System.out.println("password already changed");
        return true;
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

        return billRepository.findPaidBills(BillState.paid,client.getId()).get();
    }

    public void createClientDemand(Client client)
    {

    }

    public void addClientRequest(Demande demande) {
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

        return billRepository.billHistory(BillState.paid, currentUser.getId()).get();

    }
}
