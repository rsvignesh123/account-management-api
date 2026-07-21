package com.management.Accounts.repository;

import com.management.Accounts.entity.NotificationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface notificationRepository extends MongoRepository<NotificationModel, String> {

    List<NotificationModel> findByTenantIdOrderByCreatedAtDesc(
            String tenantId
    );


    long countByTenantIdAndReadFalse(
            String tenantId
    );


    Optional<NotificationModel> findByIdAndTenantId(
            String id,
            String tenantId
    );


    List<NotificationModel> findByTenantId(
            String tenantId
    );


    void deleteByTenantId(
            String tenantId
    );
}
