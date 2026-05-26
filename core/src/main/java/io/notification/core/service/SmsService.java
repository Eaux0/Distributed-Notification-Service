package io.notification.core.service;

import org.springframework.stereotype.Service;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.enums.Channel;
import io.notification.common.model.Message;
import io.notification.common.model.MessageDetails;
import io.notification.core.model.UserContactInfo;
import io.notification.core.model.UserPreferences;

@Service
public class SmsService extends ChannelService {

    @Override
    protected MessageDetails mapDetails(UserContactInfo userContactInfo, UserPreferences userPreferences) {
        MessageDetails smsDetails = new MessageDetails();
        smsDetails.setUserId(userContactInfo.getUserId());
        smsDetails.setChannelType(Channel.SMS);
        smsDetails.setUserContactInfo(userContactInfo.getPhoneNumber());
        smsDetails.setAllowMarketingNotifications(userPreferences.getAllowMarketingSms());
        smsDetails.setAllowEventNotifications(userPreferences.getAllowEventSms());
        smsDetails.setAllowSecurityNotifications(userPreferences.getAllowSecuritySms());
        return smsDetails;
    }

    public void routeMessage(Long userId, Message message) {
        MessageStructureValidator.checkMessageStruture(message);
        MessageDetails smsDetails = getDetails(userId);
        smsDetails.setMessage(message);
        super.routeMessage("notification.sms", smsDetails);
    }
}
