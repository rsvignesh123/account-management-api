package com.management.Accounts.repository;


import com.management.Accounts.entity.transactionModel;

import org.springframework.data.mongodb.repository.MongoRepository;



public interface transactionRepository extends MongoRepository<transactionModel,String> {

}
