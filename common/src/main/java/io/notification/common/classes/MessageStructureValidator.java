package io.notification.common.classes;

import io.notification.common.enums.Channel;
import io.notification.common.model.Event;
import io.notification.common.model.Message;
import io.notification.common.model.MessageDetails;

public final class MessageStructureValidator {

    private MessageStructureValidator() {
    }

    public static void checkMessageStruture(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        checkUserId(message.getUserId());
        checkEventStruture(message.getEvent());
        if (message.getMessageType() == null) {
            throw new IllegalArgumentException("Message type cannot be null");
        }
        if (message.getPriority() == null) {
            throw new IllegalArgumentException("Message priority cannot be null");
        }
        if (!hasText(message.getMessageText())) {
            throw new IllegalArgumentException("Message text cannot be blank");
        }
    }

    public static void checkMessageDetailsStruture(MessageDetails messageDetails) {
        if (messageDetails == null) {
            throw new IllegalArgumentException("Message details cannot be null");
        }
        checkUserId(messageDetails.getUserId());
        checkMessageStruture(messageDetails.getMessage());
        if (!messageDetails.getUserId().equals(messageDetails.getMessage().getUserId())) {
            throw new IllegalArgumentException("Message details user id does not match message user id");
        }
        if (messageDetails.getChannelType() == null) {
            throw new IllegalArgumentException("Message details channel cannot be null");
        }
        if (messageDetails.getChannelType() != messageDetails.getMessage().getEvent().getChannel()) {
            throw new IllegalArgumentException("Message details channel does not match message event channel");
        }
        if (!hasText(messageDetails.getUserContactInfo())) {
            throw new IllegalArgumentException("User contact info cannot be blank");
        }
    }

    public static void checkMessageDetailsStruture(MessageDetails messageDetails, Channel expectedChannel) {
        checkMessageDetailsStruture(messageDetails);
        if (expectedChannel == null) {
            throw new IllegalArgumentException("Expected channel cannot be null");
        }
        if (messageDetails.getChannelType() != expectedChannel) {
            throw new IllegalArgumentException("Message details channel must be " + expectedChannel);
        }
    }

    public static int checkPriorityTopicStruture(String topic, String expectedTopicPrefix) {
        if (!hasText(topic)) {
            throw new IllegalArgumentException("Kafka topic cannot be blank");
        }
        if (!hasText(expectedTopicPrefix)) {
            throw new IllegalArgumentException("Expected Kafka topic prefix cannot be blank");
        }
        if (!topic.startsWith(expectedTopicPrefix)) {
            throw new IllegalArgumentException("Kafka topic must start with " + expectedTopicPrefix);
        }

        String priorityValue = topic.substring(expectedTopicPrefix.length());
        try {
            int priority = Integer.parseInt(priorityValue);
            if (priority < 0 || priority > 2) {
                throw new IllegalArgumentException("Kafka topic priority must be between 0 and 2");
            }
            return priority;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Kafka topic priority must be numeric", exception);
        }
    }

    public static void checkUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
    }

    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static void checkEventStruture(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (!hasText(event.getEventName())) {
            throw new IllegalArgumentException("Event name cannot be blank");
        }
        if (!hasText(event.getOrigin())) {
            throw new IllegalArgumentException("Event origin cannot be blank");
        }
        if (event.getChannel() == null) {
            throw new IllegalArgumentException("Event channel cannot be null");
        }
        if (event.getBackupChannel() != null && Channel.doesNotInclude(event.getBackupChannel())) {
            throw new IllegalArgumentException("Event backup channel is invalid");
        }
    }
}
