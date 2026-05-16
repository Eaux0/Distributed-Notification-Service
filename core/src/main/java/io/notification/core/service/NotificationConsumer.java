package io.notification.core.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.core.model.Message;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class NotificationConsumer {
    public final KafkaTemplate<String, Message> kafkaTemplate;

    @KafkaListener(topics = "notification.raw", groupId = "notifications")
    public void notificationListen(Message message) {
        System.out.println("Received Message: " + message.toString());
    }
}
