package com.kacper.backend.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for managing users
 * implements UserDetailsService to provide user details
 *
 * @Author Kacper Karabinowski
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    /**
     * @param username is the email of the user
     * @return UserDetails
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
