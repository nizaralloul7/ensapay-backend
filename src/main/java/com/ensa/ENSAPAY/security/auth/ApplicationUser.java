package com.ensa.ENSAPAY.security.auth;

import com.ensa.ENSAPAY.entities.User;
import com.ensa.ENSAPAY.security.UserApplicationRole;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ApplicationUser implements UserDetails
{
    private final User user;
    private final Set<? extends GrantedAuthority> grantedAuthorities;
    @Autowired
    public ApplicationUser(User user, Set<? extends GrantedAuthority> grantedAuthorities)
    {
        this.user = user;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword()
    {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser(){
        return this.user;
    }
}