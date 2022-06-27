package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.entities.*;
import com.ensa.ENSAPAY.services.BillService;
import com.ensa.ENSAPAY.services.ClientService;
import com.ensa.ENSAPAY.services.CreditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/client")
public class ClientController
{
    private final ClientService clientService;
    private final CreditorService creditorService;
    private final BillService billService;

    @Autowired
    public ClientController(ClientService clientService, CreditorService creditorService, BillService billService)
    {
        this.clientService = clientService;
        this.creditorService = creditorService;
        this.billService = billService;
    }



    @PutMapping(value = "/client/change-password")
    public boolean changePassword(@RequestParam String newPassword)
    {
        return this.clientService.changePassword(newPassword);
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

    @PostMapping("/create-request")
    public void addClientRequest(@RequestBody Demande demande)
    {
        this.clientService.addClientRequest(demande);
    }

    @GetMapping("/creditors")
    public List<Creditor> getCreditors()
    {
        return this.creditorService.getCreditors();
    }

    @PostMapping("/creditors/{creditorId}/bill")
    public void generateBill(@PathVariable("creditorId") Long creditorId,@RequestBody List<Long> unpaidIds)
    {
        this.billService.createBill(creditorId,unpaidIds);
    }

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
