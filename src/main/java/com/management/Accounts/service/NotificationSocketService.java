package com.management.Accounts.service;

import com.management.Accounts.entity.NotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public NotificationSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public void send(NotificationModel notification){

        System.out.println(
                "Sending WebSocket : " + notification.getMessage()
        );


        messagingTemplate.convertAndSend(
                "/topic/notifications/" + notification.getTenantId(),
                notification
        );

    }

}
