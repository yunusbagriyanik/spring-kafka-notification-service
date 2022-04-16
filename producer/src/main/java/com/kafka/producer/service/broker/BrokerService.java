package com.kafka.producer.service.broker;

import com.kafka.producer.dto.Notification;

public interface BrokerService {
    void pushMessage(String topic, Notification notification);

    void logNotification(Notification request, String failMsg, String status);
}
