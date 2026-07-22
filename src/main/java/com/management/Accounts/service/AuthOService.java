package com.management.Accounts.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.management.Accounts.entity.User;
import com.management.Accounts.entity.companyProfileModel;
import com.management.Accounts.entity.registerRequest;
import com.management.Accounts.repository.UserRepository;
import com.management.Accounts.repository.companyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthOService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private companyProfileRepository companyRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(
            registerRequest request,
            MultipartFile logo
    ) throws Exception {

        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }

        String tenantId =
                UUID.randomUUID()
                        .toString()
                        .replace("-","")
                        .substring(0,8)
                        .toUpperCase();

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ADMIN");
        user.setTenantId(tenantId);

        userRepository.save(user);

        companyProfileModel company = new companyProfileModel();

        company.setTenantId(tenantId);
        company.setCompanyName(request.getCompanyName());
        company.setOwnerName(request.getOwnerName());
        company.setEmail(request.getEmail());
        company.setPhoneNumber(request.getPhoneNumber());
        company.setAddress(request.getAddress());
        company.setCity(request.getCity());
        company.setGstNumber(request.getGstNumber());

        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        if(logo!=null && !logo.isEmpty()){

            Map upload =
                    cloudinary.uploader().upload(
                            logo.getBytes(),
                            ObjectUtils.asMap(
                                    "folder","company-logo"
                            )
                    );

            company.setLogoPath(upload.get("secure_url").toString());

        }

        companyRepository.save(company);

    }

}