package com.ensa.ENSAPAY.services;

import com.ensa.ENSAPAY.entities.Agent;
import com.ensa.ENSAPAY.entities.Client;
import com.ensa.ENSAPAY.entities.ClientTemplate;
import com.ensa.ENSAPAY.entities.Demande;
import com.ensa.ENSAPAY.repositories.AgentRepository;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.repositories.DemandeRepository;
import com.ensa.ENSAPAY.security.PasswordGenerator;
import com.ensa.ENSAPAY.security.auth.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService
{
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final DemandeRepository demandeRepository;
    private final EmailServiceImpl emailService;

    @Autowired
    public AgentService(AgentRepository agentRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder, DemandeRepository demandeRepository,EmailServiceImpl emailService)
    {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.demandeRepository = demandeRepository;
        this.emailService = emailService;
    }

    public void changePassword(String newPassword)
    {
        String currentLoggedInUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Agent agent = agentRepository.findAgentByUsername(currentLoggedInUser).get();

        if(agent.isPasswordChanged())
        {
            throw new IllegalStateException("Password already changed");
        }
        System.out.println(newPassword);
        agentRepository.changePasswordInFirstLogin(passwordEncoder.encode(newPassword), true, agent.getId());
        System.out.println("Password changed successfully");


    }


    public List<Agent> getAgents()
    {
        return agentRepository.findAll();
    }

    public List<Client> getRelatedClients()
    {
        String currentLoggedInUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentAgentId = agentRepository.findAgentByUsername(currentLoggedInUser).get().getId();

        return clientRepository.getRelatedClients(currentAgentId);

    }

    public void addClient(Client client,String loginLink)
    {
        String currentLoggedInUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Agent me = agentRepository.findAgentByUsername(currentLoggedInUser).get();
        client.setCreatedBy(me);
        // thank u nizar 2
        Optional<Client> clientByUsername = clientRepository.findClientByUsername(client.getUsername());
        if(clientByUsername.isPresent())
        {
            throw new IllegalStateException("client Already Exists");
        }
        String pass = PasswordGenerator.alphaNumericString();
        System.out.println(pass);
        client.setPhone("+212"+client.getPhone());
        //Send Password to agent by email
        client.setUsername(client.getPhone());
        emailService.sendEmailWithTemplating(client.getEmail(),client.getUsername(),pass,client.getFirstName(),client.getLastName(),loginLink);
        client.setRole("CLIENT");
        client.setPassword(passwordEncoder.encode(pass));
        clientRepository.save(client);
    }

    public List<Demande> geClientsDemandes()
    {

       return this.demandeRepository.findAll();
    }

    public void approveDemande(Long demandeId,Boolean status,String loginLink)
    {
        if(status)
        {
            Client client = new Client(this.demandeRepository.findById(demandeId).get());
            String currentLoggedInUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String pass = PasswordGenerator.alphaNumericString();
            client.setPassword(passwordEncoder.encode(pass));
            Agent me = agentRepository.findAgentByUsername(currentLoggedInUser).get();
            client.setCreatedBy(me);
            emailService.sendEmailWithTemplating(client.getEmail(),client.getUsername(),pass,client.getFirstName(),client.getLastName(),loginLink);
            this.clientRepository.save(client);
        }

        this.demandeRepository.deleteById(demandeId);
    }

    public void addAgent(Agent agent,String loginLink)
    {
        String pass = PasswordGenerator.alphaNumericString();
        System.out.println(pass);
        //Send Password to agent by email
        emailService.sendEmailWithTemplating(agent.getEmail(),agent.getUsername(),pass,agent.getFirstName(),agent.getLastName(),loginLink);
        agent.setRole("AGENT");
        agent.setPassword(passwordEncoder.encode(pass));
        this.agentRepository.save(agent);
    }
}
