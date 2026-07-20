package com.management.Accounts.repository;

import com.management.Accounts.entity.orderStoreModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface orderStoreRepository extends MongoRepository<orderStoreModel,String> {

    boolean existsByCompanyNameAndOrderDateAfter(
            String companyName,
            LocalDate orderDate
    );

    Optional<orderStoreModel> findTopByOrderByBillNumberDesc();
    List<orderStoreModel> findAllByOrderByOrderDateDescCreatedAtAsc();
    long countByOrderStatusIgnoreCase(String orderStatus);
}
