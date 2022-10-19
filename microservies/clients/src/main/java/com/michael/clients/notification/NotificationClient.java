package com.michael.clients.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("notification")
public interface NotificationClient {

    @PostMapping("/api/v1/notification")
    public void  sendNotification(@RequestBody NotificationRequest notificationRequest);
}
