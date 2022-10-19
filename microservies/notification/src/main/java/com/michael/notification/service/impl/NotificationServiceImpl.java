package com.michael.notification.service.impl;

import com.michael.clients.notification.NotificationRequest;
import com.michael.notification.entity.Notification;

import com.michael.notification.repository.NotificationRepository;
import com.michael.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public void send(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .toCustomerId(notificationRequest.getToCustomerId())
                .toCustomerEmail(notificationRequest.getToCustomerEmail())
                .sender("Michael Royf")
                .message(notificationRequest.getMessage())
                .sendAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }
}
