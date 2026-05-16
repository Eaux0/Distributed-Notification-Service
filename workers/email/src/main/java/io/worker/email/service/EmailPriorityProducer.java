package io.worker.email.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.worker.email.models.MessageDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Service
public class EmailPriorityProducer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;

    private Boolean isMessageToBeSent(MessageDetails messageDetails) {
        Integer messagePriority = messageDetails.getMessage().getPriority();
        Boolean allowMarketingNotifcations = messageDetails.getAllowMarketingNotifcations();
        Boolean allowEventNotifications = messageDetails.getAllowEventNotifications();
        Boolean allowSecurityNotifications = messageDetails.getAllowSecurityNotifications();
        if (messagePriority == 0 && !allowSecurityNotifications) {
            return false;
        }
        if (messagePriority == 1 && !allowEventNotifications) {
            return false;
        }
        if (messagePriority == 2 && !allowMarketingNotifcations) {
            return false;
        }
        return true;
    }

    public Boolean routeMesage(MessageDetails messageDetails) {
        if (messageDetails.getMessage().getPriority() > 2 || messageDetails.getMessage().getPriority() < 0)
            throw new IllegalArgumentException("Invalid Priority value. Highest Priority is 0 and Lowest is 2.");
        if (!isMessageToBeSent(messageDetails))
            return false;

        String newTopic = "notification.email." + messageDetails.getMessage().getPriority();
        kafkaTemplate.send(newTopic, messageDetails);

        return true;
    }
}
