package com.management.Accounts.repository;

import com.management.Accounts.entity.NotificationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface notificationRepository extends MongoRepository<NotificationModel, String> {

    List<NotificationModel> findAllByOrderByCreatedAtDesc();

    long countByReadFalse();
}
