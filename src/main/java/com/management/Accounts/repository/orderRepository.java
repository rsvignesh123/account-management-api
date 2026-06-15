package com.management.Accounts.repository;

import com.management.Accounts.entity.orderModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface orderRepository extends MongoRepository<orderModel,String> {
    List<orderModel> findAllByOrderByOrderDateDesc();

    List<orderModel> findByOrderCompanyNameContainingIgnoreCaseOrderByOrderDateDesc(
            String companyName

    );

    List<orderModel> findByOrderDateOrderByOrderDateDesc(
            LocalDate orderDate
    );
    List<orderModel>
    findByOrderCompanyNameContainingIgnoreCaseAndOrderDateOrderByOrderDateDesc(
            String companyName,
            LocalDate orderDate);
    boolean existsByOrderCompanyNameAndOrderDateAfter(
            String companyName,
            LocalDate date
    );
    List<orderModel> findByOrderDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}