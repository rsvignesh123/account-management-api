package com.management.Accounts.repository;

import com.management.Accounts.entity.companyProfileModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface companyProfileRepository extends MongoRepository<companyProfileModel,String> {

    Optional<companyProfileModel> findByTenantId(String tenantId);

    Optional<companyProfileModel> findByIdAndTenantId(String id, String tenantId);

}
