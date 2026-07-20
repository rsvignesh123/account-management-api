package com.management.Accounts.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.management.Accounts.entity.companyProfileModel;
import com.management.Accounts.repository.companyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class companyProfileService {


    @Autowired
    private companyProfileRepository repository;

    @Autowired
    private Cloudinary cloudinary;



    public companyProfileModel saveCompany(
            companyProfileModel company,
            MultipartFile logo
    ) throws IOException {

        if (logo != null && !logo.isEmpty()) {

            Map uploadResult = cloudinary.uploader().upload(
                    logo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "company-logo",
                            "resource_type", "auto"
                    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();

            company.setLogoPath(fileUrl);
        }

        return repository.save(company);

    }

    public List<companyProfileModel> getCompany(){

        return repository.findAll();

    }
    public companyProfileModel getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }


    public companyProfileModel updateCompany(
            String id,
            companyProfileModel data,
            MultipartFile logo
    ) throws IOException {


        companyProfileModel existing =
                repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Company not found"));


        existing.setOwnerName(data.getOwnerName());

        existing.setCompanyName(data.getCompanyName());

        existing.setTagline(data.getTagline());

        existing.setAddress(data.getAddress());

        existing.setCity(data.getCity());

        existing.setPincode(data.getPincode());

        existing.setEmail(data.getEmail());

        existing.setPhoneNumber(data.getPhoneNumber());

        existing.setGstNumber(data.getGstNumber());


        // update logo only if new logo uploaded
        if (logo != null && !logo.isEmpty()) {

            Map uploadResult = cloudinary.uploader().upload(
                    logo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "company-logo",
                            "resource_type", "auto"
                    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();

            existing.setLogoPath(fileUrl);
        }


        // system generated fields
        existing.setUpdatedAt(LocalDateTime.now());


        companyProfileModel updatedCompany =
                repository.save(existing);


        return updatedCompany;
    }


    public void delete(String id){

        repository.deleteById(id);

    }

}