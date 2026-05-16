package io.notification.core.service;

import org.springframework.kafka.core.KafkaTemplate;

import io.notification.core.dto.DetailsDTO;
import io.notification.core.model.UserContactInfo;
import io.notification.core.model.UserPreferences;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public abstract class ChannelService<T extends DetailsDTO> {

    public final KafkaTemplate<String, DetailsDTO> kafkaTemplate;

    private UserContactInfo getUserContactInfo(Long userId) {
        return new UserContactInfo(userId, null, "+1234567890", "UTC", "en");
    }

    private UserPreferences getUserPreferences(Long userId) {
        return new UserPreferences(userId, true, true, true, true, true, true);
    }

    protected abstract T mapDetails(UserContactInfo userContactInfo, UserPreferences userPreferences);

    public T getDetails(Long userId) {
        UserContactInfo userContactInfo = getUserContactInfo(userId);
        UserPreferences userPreferences = getUserPreferences(userId);
        return mapDetails(userContactInfo, userPreferences);
    }

    public void routeMessage(String topic, DetailsDTO detailsDto) {
        kafkaTemplate.send(topic, detailsDto);
    }

}
