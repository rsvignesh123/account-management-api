package com.management.Accounts.controller;

import com.management.Accounts.entity.LoginRequest;
import com.management.Accounts.entity.User;



import com.management.Accounts.security.JwtUtil;
import com.management.Accounts.service.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class loginController {
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private authService AuthService;
    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        System.out.println("Username = " + request.getUsername());
        System.out.println("Password = " + request.getPassword());
        Optional<User> user =
                AuthService.findByUsername(
                        request.getUsername());
        System.out.println("User found = " + user.isPresent());

        if (user.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("User Not Found");
        }

        boolean valid =
                encoder.matches(
                        request.getPassword(),
                        user.get().getPassword()
                );

        if (!valid) {
            return ResponseEntity.badRequest()
                    .body("Invalid Password");
        }

        String token =
                jwtUtil.generateToken(
                        user.get().getUsername()
                );

        return ResponseEntity.ok(token);

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user) {

        user.setPassword(
                encoder.encode(
                        user.getPassword()
                )
        );

        AuthService.save(user);

        return ResponseEntity.ok("User Created");
    }

}