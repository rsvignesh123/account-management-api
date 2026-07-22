package com.management.Accounts.controller;

import com.management.Accounts.entity.registerRequest;
import com.management.Accounts.service.AuthOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthOService service;

    @PostMapping(
            value="/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> register(

            @ModelAttribute registerRequest request,

            @RequestParam(required = false)
            MultipartFile logo

    ) throws Exception{

        service.register(request,logo);

        return ResponseEntity.ok(
                Map.of("message","Registration Successful")
        );
    }

}