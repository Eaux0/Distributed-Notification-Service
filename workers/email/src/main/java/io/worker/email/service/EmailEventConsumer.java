package io.worker.email.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.enums.Channel;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmailEventConsumer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;
    public final EmailPriorityProducer emailPriorityProducer;

    @KafkaListener(topics = "notification.email", groupId = "notifications")
    public void notificationListen(MessageDetails messageDetails) {
        MessageStructureValidator.checkMessageDetailsStruture(messageDetails, Channel.EMAIL);
        System.out.println("Received Message: " + messageDetails.toString());

        Boolean wasMessageSent = emailPriorityProducer.routeMessage(messageDetails);
        if (!wasMessageSent)
            System.out.println("Notification Skipped due to user Preference");
    }
}
