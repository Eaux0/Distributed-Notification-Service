package io.worker.sms.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.enums.Channel;
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
        Boolean allowMarketingNotifications = messageDetails.getAllowMarketingNotifications() == null ? false
                : messageDetails.getAllowMarketingNotifications();
        Boolean allowEventNotifications = messageDetails.getAllowEventNotifications() == null ? false
                : messageDetails.getAllowEventNotifications();
        Boolean allowSecurityNotifications = messageDetails.getAllowSecurityNotifications() == null ? false
                : messageDetails.getAllowSecurityNotifications();
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
        MessageStructureValidator.checkMessageDetailsStruture(messageDetails, Channel.SMS);
        if (Priority.doesNotInclude(messageDetails.getMessage().getPriority().toString()))
            throw new IllegalArgumentException("Invalid Priority value.");
        if (!isMessageToBeSent(messageDetails))
            return false;
        Integer currentPriority = Priority.getPriority(messageDetails.getMessage().getPriority());
        String newTopic = "notification.sms." + currentPriority.toString();

        try {
            kafkaTemplate.send(newTopic, messageDetails).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            throw new IllegalStateException("Interrupted while publishing to Kafka topic " + newTopic, exception);
        } catch (ExecutionException | TimeoutException exception) {
            throw new IllegalStateException("Failed to publish to Kafka topic " + newTopic, exception);
        }

        return true;
    }
}
