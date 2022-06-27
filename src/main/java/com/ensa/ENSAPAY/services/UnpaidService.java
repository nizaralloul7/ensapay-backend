package com.ensa.ENSAPAY.services;

import com.ensa.ENSAPAY.entities.*;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.repositories.UnpaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class UnpaidService
{
    private final UnpaidRepository unpaidRepository;
    private final ClientRepository clientRepository;
    private final CreditorService creditorService;
    @Autowired
    public UnpaidService(UnpaidRepository unpaidRepository, ClientRepository clientRepository, CreditorService creditorService)
    {
        this.unpaidRepository = unpaidRepository;
        this.clientRepository = clientRepository;
        this.creditorService = creditorService;
    }


    public List<Unpaid> getUnpaids()
    {
        return unpaidRepository.findAll();
    }

    public void addUnpaid(Unpaid unpaid)
    {
        unpaidRepository.save(unpaid);
    }

    public List<Unpaid> getCreditorUnpaid(Long creditorId) {
        String authClient = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(authClient).get();
        return unpaidRepository.getUnpaidsForTheCreditorAndClient(creditorId,client.getId());
    }


    public void generateUnpaid() {
        String authClient = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(authClient).get();
        List<Creditor> creditors = creditorService.getCreditors();
        List<Unpaid> generatedUnpaid = new ArrayList<>();
        for(Creditor creditor: creditors){
            if(creditor.getType() == CreditorType.Charity){
                continue;
            }
            int count = new Random().nextInt(5) + 0;
            for(int i=0; i< count;i++){
                Unpaid unpaid = new Unpaid();
                unpaid.setClient(client);
                unpaid.setCreditor(creditor);
                int unpaidType = new Random().nextInt(2);
                unpaid.setType(UnpaidType.values()[unpaidType]);
                int amount = new Random().nextInt(200) + 50;
                unpaid.setAmount(new BigDecimal(amount));
                if(unpaid.getType() == UnpaidType.UNPAID){
                    unpaid.setDescription("This is an unpaid for " +creditor.getTitle());
                }
                else{
                    unpaid.setDescription("This is a penalty for " +creditor.getTitle());
                }

                generatedUnpaid.add(unpaid);
            }
        }
        unpaidRepository.saveAll(generatedUnpaid);
    }
}
