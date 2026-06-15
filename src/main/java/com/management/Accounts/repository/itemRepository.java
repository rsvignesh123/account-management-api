package com.management.Accounts.repository;

import com.management.Accounts.entity.itemModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface itemRepository extends MongoRepository<itemModel,String> {

    List<itemModel> findByOrderId(String orderId);
}
