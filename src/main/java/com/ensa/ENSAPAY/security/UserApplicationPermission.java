package com.ensa.ENSAPAY.security;

public enum UserApplicationPermission
{
    //Admin permissions
    ADMIN_READ("admin:read"),

    //Agent permissions
    AGENT_READ("agent:read"),
    AGENT_WRITE("agent:write"),

    //Client permissions
    CLIENT_READ("client:read"),
    CLIENT_WRITE("client:write");

    private final String permissions;


    UserApplicationPermission(String permissions)
    {
        this.permissions = permissions;
    }

    public String getPermission()
    {
        return this.permissions;
    }

}
