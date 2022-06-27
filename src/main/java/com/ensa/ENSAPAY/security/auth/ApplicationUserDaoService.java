package com.ensa.ENSAPAY.security.auth;


import com.ensa.ENSAPAY.entities.Admin;
import com.ensa.ENSAPAY.entities.Agent;
import com.ensa.ENSAPAY.entities.Client;
import com.ensa.ENSAPAY.repositories.AdminRepository;
import com.ensa.ENSAPAY.repositories.AgentRepository;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.security.UserApplicationRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class ApplicationUserDaoService implements ApplicationUserDao
{
    private final AdminRepository adminRepository;
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ApplicationUserDaoService(AdminRepository adminRepository, AgentRepository agentRepository, ClientRepository clientRepository)
    {
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username)
    {
        return getApplicationUsers().stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers()
    {
        List<Admin> admins = adminRepository.findAll();
        List<Agent> agents = agentRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        List<ApplicationUser> appUsers = new ArrayList<>();

        for(Admin admin : admins)
        {
            appUsers.add(new ApplicationUser(admin, UserApplicationRole.ADMIN.getGrantedAuthorities()));
        }

       for(Agent agent : agents)
        {
            appUsers.add(new ApplicationUser(agent, UserApplicationRole.AGENT.getGrantedAuthorities()));
        }

        for(Client client : clients)
        {
            appUsers.add(new ApplicationUser(client, UserApplicationRole.CLIENT.getGrantedAuthorities()));
        }


        return appUsers;


    }
}
