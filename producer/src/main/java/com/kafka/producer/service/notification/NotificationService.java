package com.kafka.producer.service.notification;

import com.kafka.producer.dto.Notification;

public interface NotificationService {
    void pushNotification(Notification notification);
}
