package io.notification.core.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.enums.Channel;
import io.notification.common.model.Message;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class NotificationConsumer {
    public final KafkaTemplate<String, Message> kafkaTemplate;
    public final SmsService smsService;
    public final EmailService emailService;

    @KafkaListener(topics = "notification.raw", groupId = "notifications")
    public void notificationListen(Message message) {
        System.out.println("Received Message: " + message.toString());

        if (message.getEvent().getChannel() == Channel.SMS) {
            smsService.routeMessage(message.getUserId(), message);
        } else if (message.getEvent().getChannel() == Channel.EMAIL) {
            emailService.routeMessage(message.getUserId(), message);
        } else {
            throw new IllegalArgumentException("Invalid event type: " + message.getEvent());
        }
    }
}
