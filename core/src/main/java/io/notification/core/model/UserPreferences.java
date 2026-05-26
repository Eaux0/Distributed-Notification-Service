package io.notification.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_preferences")
public class UserPreferences {
    @Id
    @Column(name = "user_id")
    Long userId;

    @Column(name = "allow_marketing_email")
    Boolean allowMarketingEmail;

    @Column(name = "allow_marketing_sms")
    Boolean allowMarketingSms;

    @Column(name = "allow_event_email")
    Boolean allowEventEmail;

    @Column(name = "allow_event_sms")
    Boolean allowEventSms;

    @Column(name = "allow_security_email")
    Boolean allowSecurityEmail;

    @Column(name = "allow_security_sms")
    Boolean allowSecuritySms;
}
