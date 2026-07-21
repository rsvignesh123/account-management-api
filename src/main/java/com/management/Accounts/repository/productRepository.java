package com.management.Accounts.repository;



import com.management.Accounts.entity.productModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface productRepository extends MongoRepository<productModel, String> {

    List<productModel> findByTenantId(
            String tenantId
    );


    Optional<productModel> findByIdAndTenantId(
            String id,
            String tenantId
    );


    Optional<productModel> findByProductNameAndTenantId(
            String productName,
            String tenantId
    );


}
