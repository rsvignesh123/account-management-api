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
import java.util.Optional;

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

        Optional<companyProfileModel> existing =
                repository.findByTenantId(company.getTenantId());

        if (existing.isPresent()) {
            throw new RuntimeException("Company Profile already exists");
        }

        if (logo != null && !logo.isEmpty()) {

            Map uploadResult = cloudinary.uploader().upload(
                    logo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "company-logo",
                            "resource_type", "auto"
                    )
            );

            company.setLogoPath(
                    uploadResult.get("secure_url").toString()
            );
        }

        return repository.save(company);
    }

    public companyProfileModel getCompany(String tenantId) {

        return repository.findByTenantId(tenantId)
                .orElseThrow(() ->
                        new RuntimeException("Company Profile not found"));

    }
    public companyProfileModel getById(
            String id,
            String tenantId) {

        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new RuntimeException("Company Profile not found"));
    }

    public companyProfileModel updateCompany(
            String id,
            String tenantId,
            companyProfileModel data,
            MultipartFile logo
    ) throws IOException {

        companyProfileModel existing =
                repository.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(() ->
                                new RuntimeException("Company not found"));

        existing.setOwnerName(data.getOwnerName());
        existing.setCompanyName(data.getCompanyName());
        existing.setTagline(data.getTagline());
        existing.setAddress(data.getAddress());
        existing.setCity(data.getCity());
        existing.setPincode(data.getPincode());
        existing.setEmail(data.getEmail());
        existing.setPhoneNumber(data.getPhoneNumber());
        existing.setGstNumber(data.getGstNumber());

        // Update logo only if new logo uploaded
        if (logo != null && !logo.isEmpty()) {

            Map uploadResult = cloudinary.uploader().upload(
                    logo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "company-logo",
                            "resource_type", "auto"
                    )
            );

            existing.setLogoPath(uploadResult.get("secure_url").toString());
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    public void delete(String id, String tenantId) {

        companyProfileModel company =
                repository.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(() ->
                                new RuntimeException("Company Profile not found"));

        repository.delete(company);

    }

}