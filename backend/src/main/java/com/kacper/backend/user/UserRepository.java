package com.kacper.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing users
 *
 * @Author Kacper Karabinowski
 */
public interface UserRepository extends JpaRepository<User, Integer>
{
    /**
     * @param email is the email of the user
     * @return Optional of the user with the specified email
     */
    Optional<User> findByEmail(String email);
}
