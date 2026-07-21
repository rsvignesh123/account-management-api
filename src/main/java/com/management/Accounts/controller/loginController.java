package com.management.Accounts.controller;


import com.management.Accounts.entity.ApiResponse;
import com.management.Accounts.entity.LoginResponse;
import com.management.Accounts.entity.User;
import com.management.Accounts.security.JwtUtil;
import com.management.Accounts.service.authService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class loginController {


    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private authService AuthService;


    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginResponse request) {


        Optional<User> user =
                AuthService.findByUsername(
                        request.getUsername()
                );


        if(user.isEmpty()){

            return ResponseEntity.badRequest()
                    .body("User Not Found");
        }


        boolean valid =
                encoder.matches(
                        request.getPassword(),
                        user.get().getPassword()
                );


        if(!valid){

            return ResponseEntity.badRequest()
                    .body("Invalid Password");

        }



        String token =
                jwtUtil.generateToken(
                        user.get().getUsername()
                );



        Map<String,Object> response =
                new HashMap<>();


        response.put(
                "token",
                token
        );


        response.put(
                "username",
                user.get().getUsername()
        );


        response.put(
                "tenantId",
                user.get().getTenantId()
        );


        response.put(
                "role",
                user.get().getRole()
        );


        return ResponseEntity.ok(response);

    }




    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user,
            @RequestHeader("tenantId") String tenantId
    ){


        user.setTenantId(tenantId);


        user.setPassword(
                encoder.encode(
                        user.getPassword()
                )
        );


        AuthService.save(user);


        return ResponseEntity.ok(
                new ApiResponse("User Created")
        );

    }

}