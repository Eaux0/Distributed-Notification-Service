package io.notification.core.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.model.MessageDetails;
import io.notification.core.model.UserContactInfo;
import io.notification.core.model.UserPreferences;
import io.notification.core.repository.UserContactInfoRepository;
import io.notification.core.repository.UserPreferencesRepository;
import lombok.Setter;

@Setter
public abstract class ChannelService {

    @Autowired
    protected KafkaTemplate<String, MessageDetails> kafkaTemplate;

    @Autowired
    protected UserContactInfoRepository userContactInfoRepository;

    @Autowired
    protected UserPreferencesRepository userPreferencesRepository;

    private UserContactInfo getUserContactInfo(Long userId) {
        UserContactInfo userContactInfo = userContactInfoRepository.findByUserId(userId);
        if (userContactInfo == null) {
            throw new IllegalArgumentException("User not found");
        }
        return userContactInfo;
    }

    private UserPreferences getUserPreferences(Long userId) {
        UserPreferences userPreferences = userPreferencesRepository.findByUserId(userId);
        if (userPreferences == null)
            throw new IllegalArgumentException("User not found");
        return userPreferences;
    }

    protected abstract MessageDetails mapDetails(UserContactInfo userContactInfo, UserPreferences userPreferences);

    public MessageDetails getDetails(Long userId) {
        MessageStructureValidator.checkUserId(userId);
        UserContactInfo userContactInfo = getUserContactInfo(userId);
        UserPreferences userPreferences = getUserPreferences(userId);
        return mapDetails(userContactInfo, userPreferences);
    }

    public void routeMessage(String topic, MessageDetails messageDetails) {
        if (!MessageStructureValidator.hasText(topic)) {
            throw new IllegalArgumentException("Kafka topic cannot be blank");
        }
        MessageStructureValidator.checkMessageDetailsStruture(messageDetails);
        try {
            kafkaTemplate.send(topic, messageDetails).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            throw new IllegalStateException("Interrupted while publishing to Kafka topic " + topic, exception);
        } catch (ExecutionException | TimeoutException exception) {
            throw new IllegalStateException("Failed to publish to Kafka topic " + topic, exception);
        }
    }

}
