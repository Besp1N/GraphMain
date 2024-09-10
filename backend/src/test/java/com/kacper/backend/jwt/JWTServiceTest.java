package com.kacper.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.junit.jupiter.api.extension.ExtendWith;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    private JWTService jwtService;

    @Mock
    private UserDetails userDetails;
    private final String secretString = "d2lzZXN0YXJlZG5lY2tlbHNlZWR1Y2F0aW9uY29zdGN1cHN0cmFpZ2h0aHVudGNoYW0=";
    private static final long EXPIRATION_TIME = 864000000;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {

        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretString);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        jwtService = new JWTService(secretString);

        when(userDetails.getUsername()).thenReturn("testuserunit");

    }

    @Test
    void checkWorking() {
        String token = jwtService.generateToken(userDetails);

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        assertThat(claims.getSubject()).isEqualTo("testuserunit");
    }


    @Test
    void generateValid() {
        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotEmpty();

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo("testuserunit");
    }

    @Test
    void extractUsername_extractValid() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("testuserunit");
    }


    @Test
    void tokenExpiredbutValid_ReturnFalse() {
        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.isTokenExpired(token)).isFalse();


    }


    @Test
    void tokenExpired_returnTrue_tokenExpired() {
        String expiredToken = Jwts.builder()
                .subject("testuserunit")
                .issuedAt(new Date(System.currentTimeMillis() - EXPIRATION_TIME))
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(secretKey)
                .compact();

        assertThat(jwtService.isTokenExpired(expiredToken)).isTrue();
    }



    @Test
    void extraCharacters_token() {
        // valid token
        String token = jwtService.generateToken(userDetails);
        // adding extra
        String tamperedToken = token + "abc";

        // make sure token is invalid
        assertThatThrownBy(() -> jwtService.isTokenValid(tamperedToken, userDetails))
                .isInstanceOf(io.jsonwebtoken.security.SignatureException.class);
    }

    @Test
    void refreshToken_validToken() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");

        String token = jwtService.generateRefreshToken(claims, userDetails);
        assertThat(token).isNotEmpty();


        Claims parsedClaims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(parsedClaims.getSubject()).isEqualTo("testuserunit");
        assertThat(parsedClaims.get("role")).isEqualTo("admin");
    }



    @Test
    void returnFalse_invalidSignature() {
        //token using correct key
        String token = jwtService.generateToken(userDetails);

        // creaating token with invalid signature
        SecretKey invalidSecretKey = Keys.hmacShaKeyFor("very-invalid-key-for-test-aaaah-very-very".getBytes());

        assertThatThrownBy(() -> {
            Jwts.parser()
                    .verifyWith(invalidSecretKey)
                    .build()
                    .parseSignedClaims(token);
            // should throw an exception
        }).isInstanceOf(io.jsonwebtoken.security.SecurityException.class);
    }

    @Test
    void tokenExpiresRightaway() throws InterruptedException {
        // very short expiration time
        String token = Jwts.builder()
                .subject("testuserunit")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1))
                .signWith(secretKey)
                .compact();
        //now we waaaaaaaiiiiiiiiiit ehe-he
        Thread.sleep(2);

        // NOW NOW GET HIM ARGH ARGH ARGH
        assertThat(jwtService.isTokenExpired(token)).isTrue();
    }

    @Test
    void tokenNoSubject() {
        String token = Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();

        // extract username - expect exception
        assertThatThrownBy(() -> jwtService.extractUsername(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("JWT does not contain a subject");
    }



    @Test
    void throwException_tokenEmpty() {
        String token = "";

        // extract username - expect exception
        assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
        assertThatThrownBy(() -> jwtService.extractUsername(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("JWT string cannot be empty or null");
    }




}