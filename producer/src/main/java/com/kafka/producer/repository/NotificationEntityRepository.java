package com.kafka.producer.repository;

import com.kafka.producer.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationEntityRepository extends MongoRepository<NotificationEntity, String> {
}
