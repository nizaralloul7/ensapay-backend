package com.ensa.ENSAPAY.security.auth;

import java.util.Optional;

public interface ApplicationUserDao
{
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
