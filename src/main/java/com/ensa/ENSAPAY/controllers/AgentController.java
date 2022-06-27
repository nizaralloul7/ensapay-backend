package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.entities.Agent;
import com.ensa.ENSAPAY.entities.Client;
import com.ensa.ENSAPAY.entities.Demande;
import com.ensa.ENSAPAY.services.AgentService;
import com.ensa.ENSAPAY.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class AgentController
{
    private final AgentService agentService;
    private final ClientService clientService;

    @Autowired
    public AgentController(AgentService agentService, ClientService clientService)
    {
        this.agentService = agentService;
        this.clientService = clientService;
    }


    @PostMapping(value = "/add-client")
    @PreAuthorize("hasAuthority('client:write')")
    public void addClient(@RequestBody Client client,@RequestParam(value = "login-link") String loginLink)
    {
        agentService.addClient(client,loginLink);
    }
    

    @PostMapping(value = "/agent/change-password")
    @PreAuthorize("hasRole('ROLE_AGENT')")
    public String changePassword(@RequestParam String newPassword)
    {
        return this.agentService.changePassword(newPassword);
    }

    @GetMapping(value = "/agent/related-clients")
    @PreAuthorize("hasAuthority('client:read')")
    public List<Client> getRelatedClients()
    {
        return agentService.getRelatedClients();
    }


    @GetMapping(value = "/get-requests")
    @PreAuthorize("hasAuthority('client:read')")
    public List<Demande> geClientsDemandes()
    {
        return agentService.geClientsDemandes();
    }


    @PostMapping(value = "/request")
    @PreAuthorize("hasAuthority('client:write')")
    public void setDemande(@RequestParam Long demandeId, @RequestParam Boolean status) { agentService.approveDemande(demandeId,status);
    }


}
