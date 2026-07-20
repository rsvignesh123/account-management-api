package com.management.Accounts.repository;


import com.management.Accounts.entity.customerModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface customerRepository extends MongoRepository<customerModel, String> {

    Optional<customerModel> findByCompanyName(String companyName);

}
