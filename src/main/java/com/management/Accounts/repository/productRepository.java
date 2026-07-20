package com.management.Accounts.repository;



import com.management.Accounts.entity.productModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface productRepository extends MongoRepository<productModel, String> {

    Optional<productModel> findByProductName(String productName);

}
