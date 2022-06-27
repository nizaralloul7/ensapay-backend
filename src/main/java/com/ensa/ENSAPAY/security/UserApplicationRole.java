package com.ensa.ENSAPAY.security;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ensa.ENSAPAY.security.UserApplicationPermission.*;

@Getter
public enum UserApplicationRole
{
    ADMIN(Sets.newHashSet(ADMIN_READ,AGENT_READ,AGENT_WRITE,CLIENT_READ,CLIENT_WRITE)),
    AGENT(Sets.newHashSet(CLIENT_READ,CLIENT_WRITE,AGENT_READ)),
    CLIENT(Sets.newHashSet());

    private final Set<UserApplicationPermission> permissions;

    UserApplicationRole(Set<UserApplicationPermission> permissions)
    {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities()
    {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));

        return permissions;
    }
}
