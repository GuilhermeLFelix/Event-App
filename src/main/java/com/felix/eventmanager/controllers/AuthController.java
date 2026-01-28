package com.felix.eventmanager.controllers;

import com.felix.eventmanager.dtos.AuthenticationDTO;
import com.felix.eventmanager.dtos.LoginResponseDTO;
import com.felix.eventmanager.dtos.RegisterDTO;
import com.felix.eventmanager.infra.security.TokenService;
import com.felix.eventmanager.models.User;
import com.felix.eventmanager.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Authenticator;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthController(final AuthenticationManager authenticationManager, final UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User)auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.findByLogin(data.login()) != null)
            return ResponseEntity.badRequest().build();

        this.userRepository.save(User.builder()
                .login(data.login())
                .password(new BCryptPasswordEncoder().encode(data.password()))
                .role(data.role())
                .build());

        return ResponseEntity.ok().build();
    }
}