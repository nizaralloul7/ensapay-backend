package com.ensa.ENSAPAY.services;

import com.ensa.ENSAPAY.entities.Creditor;
import com.ensa.ENSAPAY.repositories.CreditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditorService
{
    private final CreditorRepository creditorRepository;

    @Autowired
    public CreditorService(CreditorRepository creditorRepository)
    {
        this.creditorRepository = creditorRepository;
    }


    public void addCreditor(Creditor creditor)
    {
        Optional<Creditor> creditorByTitle = creditorRepository.findCreditorByTitle(creditor.getTitle());
        if(creditorByTitle.isPresent())
        {
            throw new IllegalStateException("creditor Already Exists");
        }
        creditorRepository.save(creditor);
    }

    public List<Creditor> getCreditors()
    {
        return creditorRepository.findAll();
    }

}
