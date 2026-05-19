package io.notification.core.service;

import org.springframework.kafka.core.KafkaTemplate;

import io.notification.common.model.MessageDetails;
import io.notification.core.model.UserContactInfo;
import io.notification.core.model.UserPreferences;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public abstract class ChannelService {

    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;

    private UserContactInfo getUserContactInfo(Long userId) {
        return new UserContactInfo(userId, null, "+1234567890", "UTC", "en");
    }

    private UserPreferences getUserPreferences(Long userId) {
        return new UserPreferences(userId, true, true, true, true, true, true);
    }

    protected abstract MessageDetails mapDetails(UserContactInfo userContactInfo, UserPreferences userPreferences);

    public MessageDetails getDetails(Long userId) {
        UserContactInfo userContactInfo = getUserContactInfo(userId);
        UserPreferences userPreferences = getUserPreferences(userId);
        return mapDetails(userContactInfo, userPreferences);
    }

    public void routeMessage(String topic, MessageDetails messageDetails) {
        kafkaTemplate.send(topic, messageDetails);
    }

}
