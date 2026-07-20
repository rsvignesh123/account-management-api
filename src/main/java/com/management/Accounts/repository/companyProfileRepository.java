package com.management.Accounts.repository;

import com.management.Accounts.entity.companyProfileModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface companyProfileRepository extends MongoRepository<companyProfileModel,String> {

}
