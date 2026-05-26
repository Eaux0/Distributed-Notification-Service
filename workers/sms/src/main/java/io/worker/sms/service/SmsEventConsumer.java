package io.worker.sms.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.enums.Channel;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SmsEventConsumer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;
    public final SmsPriorityProducer smsPriorityProducer;

    @KafkaListener(topics = "notification.sms", groupId = "notifications")
    public void notificationListen(MessageDetails messageDetails) {
        MessageStructureValidator.checkMessageDetailsStruture(messageDetails, Channel.SMS);
        System.out.println("Received Message: " + messageDetails.toString());

        Boolean wasMessageSent = smsPriorityProducer.routeMessage(messageDetails);
        if (!wasMessageSent)
            System.out.println("Notification Skipped due to user Preference");
    }
}
