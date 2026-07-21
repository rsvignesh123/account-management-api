package com.management.Accounts.service;

import com.management.Accounts.entity.NotificationModel;
import com.management.Accounts.repository.notificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final notificationRepository repository;
    @Autowired
    private NotificationSocketService socketService;
    public NotificationService(notificationRepository repository) {
        this.repository = repository;
    }


    public void saveNotification(
            String title,
            String message,
            String type,
            String action,
            String referenceId,
            String tenantId
    ) {

        System.out.println("Notification Created : " + message);


        NotificationModel notification =
                new NotificationModel(
                        title,
                        message,
                        type,
                        action,
                        referenceId
                );


        notification.setTenantId(tenantId);


        NotificationModel saved =
                repository.save(notification);


        socketService.send(saved);

    }



    public List<NotificationModel> getAll(String tenantId){

        return repository
                .findByTenantIdOrderByCreatedAtDesc(tenantId);

    }



    public long unreadCount(String tenantId){

        return repository
                .countByTenantIdAndReadFalse(tenantId);

    }



    public void markRead(
            String id,
            String tenantId
    ) {

        NotificationModel model =
                repository.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found"));


        model.setRead(true);


        repository.save(model);

    }



    public void markAllRead(
            String tenantId
    ) {

        List<NotificationModel> list =
                repository.findByTenantId(tenantId);


        list.forEach(x ->
                x.setRead(true)
        );


        repository.saveAll(list);

    }



    public void delete(
            String id,
            String tenantId
    ) {

        NotificationModel notification =
                repository.findByIdAndTenantId(id, tenantId)
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found"));


        repository.delete(notification);

    }



    public void clearAll(
            String tenantId
    ) {

        repository.deleteByTenantId(tenantId);

    }
}