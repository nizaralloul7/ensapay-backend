package com.ensa.ENSAPAY.controllers;


import com.ensa.ENSAPAY.entities.Admin;
import com.ensa.ENSAPAY.entities.Agent;
import com.ensa.ENSAPAY.services.AdminService;
import com.ensa.ENSAPAY.services.AgentService;
import com.ensa.ENSAPAY.services.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class AdminController
{
    private final AdminService adminService;
    private final AgentService agentService;


    @Autowired
    public AdminController(AdminService adminService, AgentService agentService)
    {
        this.adminService = adminService;
        this.agentService = agentService;
    }


    @PostMapping(value = "/add-agent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addAgent(@RequestBody Agent agent,@RequestParam(value = "login-link") String loginLink)
    {
        agentService.addAgent(agent,loginLink);
        System.out.println("passed");
    }

    @GetMapping(value = "/admins")
   // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Admin> getAdmins()
    {
        System.out.println("getting admins");
        return this.adminService.getAdmins();
    }

    @GetMapping(path = "/agents")
    @PreAuthorize("hasAnyAuthority('admin:read','agent:read')")
    public List<Agent> getAgents()
    {
        System.out.println("getAgents has been called");
        return this.agentService.getAgents();
    }




//    @DeleteMapping(path = "{adminId}")
//  //  @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasAuthority('admin:write')")
//    public void deleteAdmin(@PathVariable("adminId") Long adminId)
//    {
//        this.adminService.deleteAdmin(adminId);
//    }
}
