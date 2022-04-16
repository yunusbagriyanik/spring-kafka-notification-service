package com.kafka.producer.service.notification;

import com.kafka.producer.dto.Notification;
import com.kafka.producer.service.broker.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final BrokerService brokerService;

    @Value("${producer.kafka.topic}")
    private String topic;

    @Autowired
    public NotificationServiceImpl(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @Override
    public void pushNotification(Notification notification) {
        this.brokerService.pushMessage(topic, notification);
    }
}
