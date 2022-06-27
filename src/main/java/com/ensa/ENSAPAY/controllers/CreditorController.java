package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.entities.Creditor;
import com.ensa.ENSAPAY.services.CreditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/creditors")
public class CreditorController
{
    private final CreditorService creditorService;

    @Autowired
    public CreditorController(CreditorService creditorService)
    {
        this.creditorService = creditorService;
    }

    @PostMapping
    public void addCreditor(@RequestBody Creditor creditor) { this.creditorService.addCreditor(creditor);}

    @GetMapping
    public List<Creditor> getCreditors()
    {
        return this.creditorService.getCreditors();
    }


}
