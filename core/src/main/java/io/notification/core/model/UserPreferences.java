package io.notification.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPreferences {
    Long userId;
    Boolean allowMarketingEmail;
    Boolean allowMarketingSms;
    Boolean allowEventEmail;
    Boolean allowEventSms;
    Boolean allowSecurityEmail;
    Boolean allowSecuritySms;
}
