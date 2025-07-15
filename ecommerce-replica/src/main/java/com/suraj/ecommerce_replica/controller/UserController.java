package com.suraj.ecommerce.controller;

import com.suraj.ecommerce.entity.User;
import com.suraj.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {

    private UserRepository userRepo;

    @GetMapping("/users")
    public ResponseEntity<List<User>> GetAllUsers() {
        List<User> users = userRepo.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> GetUserByUsername(@PathVariable String username) {
        return userRepo.findByUsername(username)
                .map(user-> ResponseEntity.ok(user))
                .orElseThrow(() -> new UsernameNotFoundException("user not found with username" + username));
    }
}
