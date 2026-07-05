package com.management.Accounts.repository;

import com.management.Accounts.entity.orderModel;
import com.management.Accounts.entity.orderStoreModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface orderStoreRepository extends MongoRepository<orderStoreModel,String> {

}
