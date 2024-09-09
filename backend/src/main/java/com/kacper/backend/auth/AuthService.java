package com.kacper.backend.auth;

import com.kacper.backend.exception.InvalidCredentialsException;
import com.kacper.backend.exception.ResourceAlreadyExistException;
import com.kacper.backend.jwt.JWTService;
import com.kacper.backend.mail.MailService;
import com.kacper.backend.user.User;
import com.kacper.backend.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service for handling auth requests
 *
 * @Author Kacper Karabinowski
 */
@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    /**
     * @param userRepository repository for handling user data
     * @param passwordEncoder encoder for encoding passwords
     * @param jwtService service for handling jwt tokens
     * @param authenticationManager manager for handling authentication
     * @param mailService service for handling mail requests
     */
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService,
            AuthenticationManager authenticationManager,
            MailService mailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailService = mailService;
    }

    /**
     * Register a new user function
     *
     * @param authRegistrationRequest request for registration
     * @return AuthRegistrationResponse with user info
     * @throws ResourceAlreadyExistException if user with email already exists
     */
    public AuthRegistrationResponse register(AuthRegistrationRequest authRegistrationRequest) {
        User user = User.builder()
                .email(authRegistrationRequest.email())
                .password(passwordEncoder.encode(authRegistrationRequest.password()))
                .name(capitalizeFirstLetter(authRegistrationRequest.name()))
                .lastName(capitalizeFirstLetter(authRegistrationRequest.lastName()))
                .role(authRegistrationRequest.role().toUpperCase())
                .createdAt(LocalDateTime.now())
                .build();

        try {
            User savedUser = userRepository.save(user);
            mailService.sendMail(savedUser.getEmail());
            return AuthRegistrationResponse.builder()
                    .name(savedUser.getName())
                    .lastName(savedUser.getLastName())
                    .email(savedUser.getEmail())
                    .role(savedUser.getRole())
                    .build();
        } catch (Exception e) {
            throw new ResourceAlreadyExistException("User with email: " + user.getEmail() + " already exists");
        }
    }

    /**
     * Authenticate user function
     *
     * @param authLoginRequest request for login
     * @return AuthLoginResponse with jwt token
     * @throws InvalidCredentialsException if credentials are invalid
     */
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {
        User user = userRepository.findByEmail(authLoginRequest.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authLoginRequest.email(), authLoginRequest.password()));
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return AuthLoginResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }

    private String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
