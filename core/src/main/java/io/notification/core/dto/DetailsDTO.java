package io.notification.core.dto;

import io.notification.core.model.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DetailsDTO {
    Long userId;
    Message message;
    Boolean allowMarketingNotifcations;
    Boolean allowEventNotifications;
    Boolean allowSecurityNotifications;
}
