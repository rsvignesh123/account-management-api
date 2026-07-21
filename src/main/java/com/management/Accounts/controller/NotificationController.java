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
    public List<NotificationModel> getAll(
            @RequestHeader("tenantId") String tenantId) {

        return service.getAll(tenantId);

    }


    @GetMapping("/count")
    public long unreadCount(
            @RequestHeader("tenantId") String tenantId) {

        return service.unreadCount(tenantId);

    }


    @PutMapping("/{id}/read")
    public void markRead(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId) {

        service.markRead(id, tenantId);

    }


    @PutMapping("/read-all")
    public void markAllRead(
            @RequestHeader("tenantId") String tenantId) {

        service.markAllRead(tenantId);

    }


    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable String id,
            @RequestHeader("tenantId") String tenantId) {

        service.delete(id, tenantId);

    }


    @DeleteMapping
    public void clearAll(
            @RequestHeader("tenantId") String tenantId) {

        service.clearAll(tenantId);

    }

}