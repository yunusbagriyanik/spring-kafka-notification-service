package com.kafka.producer.service.broker;

public interface BrokerService {
    void pushMessage(String topic, String message);
}
