package com.suraj.ecommerce.controller;

import com.suraj.ecommerce.dto.*;
import com.suraj.ecommerce.entity.User;
import com.suraj.ecommerce.repository.UserRepository;
import com.suraj.ecommerce.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User.Role roleFromRequest;
        switch (request.getRole().toLowerCase()) {
            case "user":
                roleFromRequest = User.Role.USER;
                break;
            case "admin":
                roleFromRequest = User.Role.ADMIN;
                break;
            default:
                throw new IllegalArgumentException();
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(roleFromRequest)
                .build();


        userRepo.save(user);
        System.out.println(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepo.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        System.out.println("SUCCES");
        String token = jwtUtil.generateToken(request.getUsername());
        System.out.println("token:"+ token);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
