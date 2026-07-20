package com.management.Accounts.service;

import com.management.Accounts.repository.orderStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    @Autowired
    NotificationService notificationService;
    @Autowired
    orderStoreRepository repository;
    @Scheduled(cron = "0 0 9 * * *")
    public void pendingOrderReminder() {
        long pending = repository.countByOrderStatusIgnoreCase("Pending");

        if (pending > 0) {

            notificationService.saveNotification(
                    "Pending Orders",
                    "You have " + pending + " pending orders. Please review them.",
                    "ORDER_REMAINDER",
                    "REMINDER",
                    null
            );

            System.out.println("Pending notification created");
        }
    }
    }


