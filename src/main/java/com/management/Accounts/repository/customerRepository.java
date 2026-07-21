package com.management.Accounts.repository;


import com.management.Accounts.entity.customerModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface customerRepository extends MongoRepository<customerModel, String> {

    List<customerModel> findByTenantId(String tenantId);

    Optional<customerModel> findByIdAndTenantId(String id, String tenantId);

    Optional<customerModel> findByCompanyNameAndTenantId(
            String companyName,
            String tenantId
    );

}
