package com.ensa.ENSAPAY.security.auth;


import com.ensa.ENSAPAY.entities.Admin;
import com.ensa.ENSAPAY.entities.Agent;
import com.ensa.ENSAPAY.entities.Client;
import com.ensa.ENSAPAY.entities.User;
import com.ensa.ENSAPAY.repositories.AdminRepository;
import com.ensa.ENSAPAY.repositories.AgentRepository;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.security.UserApplicationRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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

        String[] usernameAndDomain = StringUtils.split(
                username, String.valueOf(Character.LINE_SEPARATOR));
        if (usernameAndDomain == null || usernameAndDomain.length != 2) {
            throw new UsernameNotFoundException("Username and domain must be provided");
        }
        return getApplicationUsers(usernameAndDomain[1]).stream()
                .filter(applicationUser -> usernameAndDomain[0].equals(applicationUser.getUsername() ))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers(String role)
    {
        List<ApplicationUser> appUsers = new ArrayList<>();
        switch (role){
            case "ADMIN" :
                List<Admin> admins = adminRepository.findAll();
                for(Admin admin : admins)
                {
                    appUsers.add(new ApplicationUser(admin, UserApplicationRole.ADMIN.getGrantedAuthorities()));
                }

                break;
            case "AGENT" :
                List<Agent> agents = agentRepository.findAll();
                for(Agent agent : agents)
                {
                    appUsers.add(new ApplicationUser(agent, UserApplicationRole.AGENT.getGrantedAuthorities()));
                }
                break;
            case "CLIENT" :
                List<Client> clients = clientRepository.findAll();
                for(Client client : clients)
                {
                    appUsers.add(new ApplicationUser(client, UserApplicationRole.CLIENT.getGrantedAuthorities()));
                }
                break;
        }
        return appUsers;


    }
}
