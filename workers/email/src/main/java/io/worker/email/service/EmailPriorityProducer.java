package io.worker.email.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.enums.Priority;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Service
public class EmailPriorityProducer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;

    private Boolean isMessageToBeSent(MessageDetails messageDetails) {
        Priority messagePriority = messageDetails.getMessage().getPriority();
        Boolean allowMarketingNotifications = messageDetails.getAllowMarketingNotifications();
        Boolean allowEventNotifications = messageDetails.getAllowEventNotifications();
        Boolean allowSecurityNotifications = messageDetails.getAllowSecurityNotifications();
        if (messagePriority == Priority.HIGHEST && !allowSecurityNotifications) {
            return false;
        }
        if (messagePriority == Priority.NORMAL && !allowEventNotifications) {
            return false;
        }
        if (messagePriority == Priority.LOWEST && !allowMarketingNotifications) {
            return false;
        }
        return true;
    }

    public Boolean routeMessage(MessageDetails messageDetails) {
        if (Priority.doesNotInclude(messageDetails.getMessage().getPriority().toString()))
            throw new IllegalArgumentException("Invalid Priority value");
        if (!isMessageToBeSent(messageDetails))
            return false;

        String newTopic = "notification.email." + messageDetails.getMessage().getPriority();
        kafkaTemplate.send(newTopic, messageDetails);

        return true;
    }
}
