package com.management.Accounts.repository;

import com.management.Accounts.entity.orderStoreModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface    orderStoreRepository extends MongoRepository<orderStoreModel,String> {

    boolean existsByCompanyNameAndOrderDateAfter(
            String companyName,
            LocalDate orderDate
    );


    // Bill number tenant wise
    Optional<orderStoreModel> findTopByTenantIdOrderByBillNumberDesc(
            String tenantId
    );


    // Get all orders tenant wise
    List<orderStoreModel> findByTenantIdOrderByOrderDateDescCreatedAtAsc(
            String tenantId
    );


    // Get single order tenant wise
    Optional<orderStoreModel> findByIdAndTenantId(
            String id,
            String tenantId
    );


    // Status count tenant wise
    long countByTenantIdAndOrderStatusIgnoreCase(
            String tenantId,
            String orderStatus
    );

    List<String> findDistinctTenantIdBy();
    // Search duplicate order tenant wise
    boolean existsByTenantIdAndCompanyNameAndOrderDateAfter(
            String tenantId,
            String companyName,
            LocalDate orderDate
    );
}
