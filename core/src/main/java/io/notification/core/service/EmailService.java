package io.notification.core.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.core.dto.DetailsDTO;
import io.notification.core.dto.EmailDetailsDTO;
import io.notification.core.model.Message;
import io.notification.core.model.UserContactInfo;
import io.notification.core.model.UserPreferences;

@Service
public class EmailService extends ChannelService<EmailDetailsDTO> {

    public EmailService(KafkaTemplate<String, DetailsDTO> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected EmailDetailsDTO mapDetails(UserContactInfo userContactInfo, UserPreferences userPreferences) {
        EmailDetailsDTO emailDetails = new EmailDetailsDTO();
        emailDetails.setUserId(userContactInfo.getUserId());
        emailDetails.setEmailId(userContactInfo.getEmail());
        emailDetails.setAllowMarketingNotifcations(userPreferences.getAllowMarketingEmail());
        emailDetails.setAllowEventNotifications(userPreferences.getAllowEventEmail());
        emailDetails.setAllowSecurityNotifications(userPreferences.getAllowSecurityEmail());
        return emailDetails;
    }

    public void routeMessage(Long userId, Message message) {
        EmailDetailsDTO emailDetails = getDetails(userId);
        emailDetails.setMessage(message);
        super.routeMessage("notification.email", emailDetails);
    }
}
