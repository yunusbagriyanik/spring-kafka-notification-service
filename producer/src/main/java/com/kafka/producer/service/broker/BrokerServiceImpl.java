package com.kafka.producer.service.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.dto.Notification;
import com.kafka.producer.entity.NotificationEntity;
import com.kafka.producer.exception.MapperException;
import com.kafka.producer.repository.NotificationEntityRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Service
public class BrokerServiceImpl implements BrokerService {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final NotificationEntityRepository notificationEntityRepository;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public BrokerServiceImpl(KafkaTemplate<Integer, String> kafkaTemplate, NotificationEntityRepository notificationEntityRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationEntityRepository = notificationEntityRepository;
    }

    @Override
    public void pushMessage(String topic, Notification notification) {
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(topic, this.jsonToString(notification));

        listenableFuture.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                logNotification(notification, null, "success");
                logger.info("Message sent. ='{}' with offset={}", notification.getContent(), result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                logNotification(notification, ex.getMessage(), "fail");
                logger.error("The message could not be sent. ='{}'", notification.getContent(), ex);
            }
        });
    }

    @Override
    public void logNotification(Notification request, String failMsg, String status) {
        NotificationEntity notification = new NotificationEntity();
        notification.setId(UUID.randomUUID().toString());
        notification.setNotification(this.jsonToString(request));
        notification.setType(request.getType());
        notification.setStatus(status);

        if (failMsg != null) {
            notification.setFailMsg(failMsg);
        }

        this.notificationEntityRepository.save(notification);
    }

    private <T> String jsonToString(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }
}
