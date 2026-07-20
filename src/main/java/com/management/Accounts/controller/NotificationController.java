package com.management.Accounts.controller;

import com.management.Accounts.entity.NotificationModel;
import com.management.Accounts.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public List<NotificationModel> getAll() {

        return service.getAll();

    }

    @GetMapping("/count")
    public long unreadCount() {

        return service.unreadCount();

    }

    @PutMapping("/{id}/read")
    public void markRead(
            @PathVariable String id) {

        service.markRead(id);

    }

    @PutMapping("/read-all")
    public void markAllRead() {

        service.markAllRead();

    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable String id) {

        service.delete(id);

    }

    @DeleteMapping
    public void clearAll() {

        service.clearAll();

    }

}