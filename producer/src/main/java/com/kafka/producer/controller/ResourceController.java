package com.kafka.producer.controller;

import com.kafka.producer.dto.Notification;
import com.kafka.producer.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class ResourceController {

    private final NotificationService notificationService;

    @Autowired
    public ResourceController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/push")
    public ResponseEntity<Void> sendNotification(@RequestBody Notification notification) {
        this.notificationService.pushNotification(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
