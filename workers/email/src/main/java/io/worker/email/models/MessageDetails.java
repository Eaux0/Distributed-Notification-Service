package io.worker.email.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDetails {
    Long userId;
    Message message;
    Boolean allowMarketingNotifcations;
    Boolean allowEventNotifications;
    Boolean allowSecurityNotifications;
    String emailId;
}
