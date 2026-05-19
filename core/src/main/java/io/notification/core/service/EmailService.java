package io.notification.core.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.enums.Channel;
import io.notification.common.model.Message;
import io.notification.common.model.MessageDetails;
import io.notification.core.model.UserContactInfo;
import io.notification.core.model.UserPreferences;

@Service
public class EmailService extends ChannelService {

    public EmailService(KafkaTemplate<String, MessageDetails> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected MessageDetails mapDetails(UserContactInfo userContactInfo, UserPreferences userPreferences) {
        MessageDetails emailDetails = new MessageDetails();
        emailDetails.setUserId(userContactInfo.getUserId());
        emailDetails.setChannelType(Channel.EMAIL);
        emailDetails.setUserContactInfo(userContactInfo.getEmail());
        emailDetails.setAllowMarketingNotifications(userPreferences.getAllowMarketingEmail());
        emailDetails.setAllowEventNotifications(userPreferences.getAllowEventEmail());
        emailDetails.setAllowSecurityNotifications(userPreferences.getAllowSecurityEmail());
        return emailDetails;
    }

    public void routeMessage(Long userId, Message message) {
        MessageDetails emailDetails = getDetails(userId);
        emailDetails.setMessage(message);
        super.routeMessage("notification.email", emailDetails);
    }
}
