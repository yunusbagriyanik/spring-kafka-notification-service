package com.kafka.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.consumer.dto.Notification;
import com.kafka.consumer.exception.MapperException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private static final ObjectMapper mapper = new ObjectMapper();
    public final SimpMessagingTemplate template;

    @Autowired
    public KafkaListeners(SimpMessagingTemplate template) {
        this.template = template;
    }

    @KafkaListener(
            topics = "${consumer.kafka.topic}",
            groupId = "${consumer.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listener(@Payload String data) {
        Notification notification = this.fromJson(data, Notification.class);
        logger.info("Received message: " + data);
        template.convertAndSend("/topic/notify", notification);
    }

    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }
}
