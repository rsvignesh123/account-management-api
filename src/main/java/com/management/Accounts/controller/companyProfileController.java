package com.management.Accounts.controller;


import com.management.Accounts.entity.companyProfileModel;

import com.management.Accounts.service.companyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/company-profile")
@CrossOrigin
public class companyProfileController {


    @Autowired
    private companyProfileService service;



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public companyProfileModel save(

            @ModelAttribute companyProfileModel company,

            @RequestParam(
                    value="logo",
                    required=false
            )
            MultipartFile logo

    ) throws Exception {

        LocalDateTime now = LocalDateTime.now();

        company.setCreatedAt(now);
        company.setUpdatedAt(now);
        return service.saveCompany(company,logo);

    }


    @GetMapping
    public List<companyProfileModel> get(){

        return service.getCompany();

    }

    @GetMapping("/{id}")
    public companyProfileModel getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PutMapping(
            value="/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public companyProfileModel update(

            @PathVariable String id,

            @ModelAttribute companyProfileModel company,

            @RequestParam(
                    value="logo",
                    required=false
            )
            MultipartFile logo

    ) throws Exception {


        return service.updateCompany(
                id,
                company,
                logo
        );

    }



    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable String id
    ){

        service.delete(id);

    }

}