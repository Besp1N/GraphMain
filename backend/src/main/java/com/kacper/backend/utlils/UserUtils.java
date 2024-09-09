package com.kacper.backend.utlils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for getting user details
 *
 * @Author Kacper Karabinowski
 */
public class UserUtils
{
    /**
     * @return UserDetails of the user
     * @throws RuntimeException if the user is not found
     */
    public static UserDetails getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        throw new RuntimeException("User not found");
    }
}
