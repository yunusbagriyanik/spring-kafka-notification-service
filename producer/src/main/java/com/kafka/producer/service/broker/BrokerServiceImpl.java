package com.kafka.producer.service.broker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class BrokerServiceImpl implements BrokerService {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    public BrokerServiceImpl(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void pushMessage(String topic, String message) {
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(topic, message);

        listenableFuture.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                logger.info("Message sent. ='{}' with offset={}", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                logger.error("The message could not be sent. ='{}'", message, ex);
            }
        });
    }
}
