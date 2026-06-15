package com.management.Accounts.repository;


import com.management.Accounts.entity.customerModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface customerRepository extends MongoRepository<customerModel, String> {

}
