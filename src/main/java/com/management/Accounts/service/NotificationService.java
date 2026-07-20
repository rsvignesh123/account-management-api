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
            String referenceId) {
        System.out.println("Notification Created : " + message);
        NotificationModel notification =
                new NotificationModel(
                        title,
                        message,
                        type,
                        action,
                        referenceId
                );

        NotificationModel saved = repository.save(notification);

        // Send notification through WebSocket
        socketService.send(saved);
    }

    public List<NotificationModel> getAll() {

        return repository.findAllByOrderByCreatedAtDesc();

    }

    public long unreadCount() {

        return repository.countByReadFalse();

    }

    public void markRead(String id) {

        NotificationModel model =
                repository.findById(id).orElseThrow();

        model.setRead(true);

        repository.save(model);

    }

    public void markAllRead() {

        List<NotificationModel> list =
                repository.findAll();

        list.forEach(x -> x.setRead(true));

        repository.saveAll(list);

    }

    public void delete(String id) {

        repository.deleteById(id);

    }

    public void clearAll() {

        repository.deleteAll();

    }
}