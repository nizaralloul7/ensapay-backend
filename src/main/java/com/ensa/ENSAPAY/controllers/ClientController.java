package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.entities.*;
import com.ensa.ENSAPAY.services.BillService;
import com.ensa.ENSAPAY.services.ClientService;
import com.ensa.ENSAPAY.services.CreditorService;
import com.ensa.ENSAPAY.services.UnpaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class ClientController
{
    private final ClientService clientService;
    private final CreditorService creditorService;
    private final BillService billService;
    private final UnpaidService unpaidService;

    @Autowired
    public ClientController(ClientService clientService, CreditorService creditorService, BillService billService, UnpaidService unpaidService)
    {
        this.clientService = clientService;
        this.creditorService = creditorService;
        this.billService = billService;
        this.unpaidService = unpaidService;
    }

/*    @GetMapping
    public List<Client> getClients()
    {
        return this.clientService.getClients();
    }*/

    @PostMapping(value = "/client/change-password")
    public void changePassword(@RequestParam("new-password") String newPassword)
    {
        this.clientService.changePassword(newPassword);
    }

    @GetMapping("/client/bills")
    public List<Bill> getPaidBills()
    {
        return this.clientService.getPaidBills();
    }

/*
    @PostMapping()
    public void createClientDemand(@RequestBody Client client)
    {
        clientService.createClientDemand(client);
    }
*/

    @PostMapping("/client/create-request")
    public void addClientRequest(@RequestBody Demande demande)
    {
        this.clientService.addClientRequest(demande);
    }

    @GetMapping("/client/creditors")
    public List<Creditor> getCreditors()
    {
        return this.creditorService.getCreditors();
    }

    @GetMapping("/client/creditors/{creditorId}/unpaid")
    public List<Unpaid> getCreditorUnpaid(@PathVariable("creditorId") Long creditorId)
    {
        return this.unpaidService.getCreditorUnpaid(creditorId);
    }

    @PostMapping("/client/creditors/{creditorId}/bill")
    public Long generateBill(@PathVariable("creditorId") Long creditorId,@RequestBody List<Long> unpaidIds)
    {
        return this.billService.createBill(creditorId,unpaidIds);
    }
    @PostMapping("/client/creditors/{creditorId}/charity")
    public Long generateCharity(@PathVariable("creditorId") Long creditorId,@RequestParam BigDecimal amount)
    {
        return this.billService.generateCharity(creditorId,amount);
    }


    @PostMapping("/me/generate-unpaid")
    public void generateUnpaid(){ unpaidService.generateUnpaid();}

    @GetMapping("/me")
    public Client getMyInfos()
    {
        return clientService.getMyInfos();
    }


    @GetMapping("/me/history")
    public List<Bill> getHistory()
    {
        return clientService.getHistory();
    }

}
