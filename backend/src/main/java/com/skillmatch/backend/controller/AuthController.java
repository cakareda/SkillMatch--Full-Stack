package com.skillmatch.backend.controller;

import com.skillmatch.backend.dto.LoginRequest;
import com.skillmatch.backend.dto.LoginResponse;
import com.skillmatch.backend.service.JwtService;
import com.skillmatch.backend.service.MyUserDetailsService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            MyUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Handle user login and return JWT token
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // load user details and generate JWT
        UserDetails user = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(user);

        // return token in response DTO
        return new LoginResponse(token);
    }
}
