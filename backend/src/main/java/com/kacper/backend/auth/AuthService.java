package com.kacper.backend.auth;

import com.kacper.backend.exception.InvalidCredentialsException;
import com.kacper.backend.exception.ResourceNotFoundException;
import com.kacper.backend.jwt.JWTService;
import com.kacper.backend.user.User;
import com.kacper.backend.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<User> register(AuthRegistrationRequest authRegistrationRequest) {
        User user = User.builder()
                .email(authRegistrationRequest.email())
                .password(passwordEncoder.encode(authRegistrationRequest.password()))
                .name(authRegistrationRequest.name())
                .lastName(authRegistrationRequest.lastName())
                .role(authRegistrationRequest.role())
                .createdAt(LocalDateTime.now())
                .build();

        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {
        User user = userRepository.findByEmail(authLoginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authLoginRequest.email(), authLoginRequest.password()));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return AuthLoginResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }
}
