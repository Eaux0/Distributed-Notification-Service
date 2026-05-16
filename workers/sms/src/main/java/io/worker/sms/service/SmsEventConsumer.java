package io.worker.sms.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.worker.sms.models.MessageDetails;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SmsEventConsumer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;
    public final SmsPriorityProducer smsPriorityProducer;

    @KafkaListener(topics = "notification.sms", groupId = "notifications")
    public void notificationListen(MessageDetails messageDetails) {
        System.out.println("Received Message: " + messageDetails.toString());

        Boolean wasMessageSent = smsPriorityProducer.routeMesage(messageDetails);
        if (!wasMessageSent)
            System.out.println("Notification Skipped due to user Preference");
    }
}
