package com.kafka.producer.service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.dto.Notification;
import com.kafka.producer.exception.MapperException;
import com.kafka.producer.service.broker.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final BrokerService brokerService;

    @Value("${producer.kafka.topic}")
    private String topic;

    @Autowired
    public NotificationServiceImpl(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @Override
    public void pushNotification(Notification notification) {
        this.brokerService.pushMessage(topic, this.toJson(notification));
    }

    private <T> String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }
}
