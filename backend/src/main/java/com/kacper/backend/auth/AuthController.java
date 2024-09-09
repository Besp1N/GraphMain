package com.kacper.backend.auth;

import com.kacper.backend.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for handling auth requests
 *
 * @Author Kacper Karabinowski
 */
@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final AuthService authService;

    /**
     * @param authService service for handling auth requests
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * @param authRegistrationRequest request for registration
     * @return AuthRegistrationResponse with user info. Status code 201 created
     */
    @PostMapping("/register")
    public ResponseEntity<AuthRegistrationResponse> register(
            @Valid @RequestBody AuthRegistrationRequest authRegistrationRequest
    ) {
        return new ResponseEntity<>(authService.register(authRegistrationRequest), HttpStatus.CREATED);
    }

    /**
     * @param authLoginRequest request for login
     * @return AuthLoginResponse with jwt token
     */
    @PostMapping("/login")
    public  AuthLoginResponse login(@RequestBody AuthLoginRequest authLoginRequest) {
        return authService.login(authLoginRequest);
    }
}
