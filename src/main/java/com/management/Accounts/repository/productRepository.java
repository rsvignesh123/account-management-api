package com.management.Accounts.repository;


import com.management.Accounts.entity.productModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface productRepository extends MongoRepository<productModel, String> {

}
