package com.kacper.backend.fixtures;

import com.kacper.backend.user.User;
import com.kacper.backend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for adding fixtures to the database
 *
 * @Author Kacper Karabinowski
 */
@Component
public class Fixtures implements CommandLineRunner
{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(Fixtures.class.getName());

    /**
     * @param passwordEncoder password encoder for encoding passwords
     * @param userRepository repository to save users
     */
    public Fixtures(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Saves admin user at first app run
     *
     * @param args command line arguments
     */
    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin.admin@fixtures.com").isPresent()) {
            return;
        }

        User user = User.builder()
                .name("Admin")
                .lastName("Admin")
                .email("admin.admin@fixtures.com")
                .password(passwordEncoder.encode("admin_password"))
                .role("ADMIN")
                .createdAt(LocalDateTime.now())
                .build();

        try {
            userRepository.save(user);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Check db connection");
        }
    }
}
