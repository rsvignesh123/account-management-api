package com.management.Accounts.repository;


import com.management.Accounts.entity.transactionModel;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface transactionRepository extends MongoRepository<transactionModel,String> {
    List<transactionModel> findByTenantId(
            String tenantId
    );


    Optional<transactionModel> findByIdAndTenantId(
            String id,
            String tenantId
    );

}
