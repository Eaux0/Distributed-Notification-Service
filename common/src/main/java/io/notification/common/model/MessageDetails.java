package io.notification.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.notification.common.enums.Channel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDetails {
    Long userId;
    Message message;
    Channel channelType;
    String userContactInfo;
    Boolean allowMarketingNotifications;
    Boolean allowEventNotifications;
    Boolean allowSecurityNotifications;
}
