package io.worker.sms.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.enums.MessageType;
import io.notification.common.enums.Priority;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Service
public class SmsPriorityProducer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;

    private Boolean isMessageToBeSent(MessageDetails messageDetails) {
        MessageType messageType = messageDetails.getMessage().getMessageType();
        Boolean allowMarketingNotifications = messageDetails.getAllowMarketingNotifications();
        Boolean allowEventNotifications = messageDetails.getAllowEventNotifications();
        Boolean allowSecurityNotifications = messageDetails.getAllowSecurityNotifications();
        if (messageType == MessageType.SECURITY && !allowSecurityNotifications) {
            return false;
        }
        if (messageType == MessageType.EVENT && !allowEventNotifications) {
            return false;
        }
        if (messageType == MessageType.MARKETING && !allowMarketingNotifications) {
            return false;
        }
        return true;
    }

    public Boolean routeMessage(MessageDetails messageDetails) {
        if (Priority.doesNotInclude(messageDetails.getMessage().getPriority().toString()))
            throw new IllegalArgumentException("Invalid Priority value.");
        if (!isMessageToBeSent(messageDetails))
            return false;
        Integer currentPriority = Priority.getPriority(messageDetails.getMessage().getPriority());
        String newTopic = "notification.sms." + currentPriority.toString();
        kafkaTemplate.send(newTopic, messageDetails);

        return true;
    }
}
