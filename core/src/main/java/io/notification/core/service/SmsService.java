package io.notification.core.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.core.dto.DetailsDTO;
import io.notification.core.dto.SmsDetailsDTO;
import io.notification.core.model.Message;
import io.notification.core.model.UserContactInfo;
import io.notification.core.model.UserPreferences;

@Service
public class SmsService extends ChannelService<SmsDetailsDTO> {

    public SmsService(KafkaTemplate<String, DetailsDTO> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected SmsDetailsDTO mapDetails(UserContactInfo userContactInfo, UserPreferences userPreferences) {
        SmsDetailsDTO smsDetails = new SmsDetailsDTO();
        smsDetails.setUserId(userContactInfo.getUserId());
        smsDetails.setPhoneNumber(userContactInfo.getPhoneNumber());
        smsDetails.setAllowMarketingNotifcations(userPreferences.getAllowMarketingSms());
        smsDetails.setAllowEventNotifications(userPreferences.getAllowEventSms());
        smsDetails.setAllowSecurityNotifications(userPreferences.getAllowSecuritySms());
        return smsDetails;
    }

    public void routeMessage(Long userId, Message message) {
        SmsDetailsDTO smsDetails = getDetails(userId);
        smsDetails.setMessage(message);
        super.routeMessage("notification.sms", smsDetails);
    }
}
