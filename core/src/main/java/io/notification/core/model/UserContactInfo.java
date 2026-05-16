package io.notification.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserContactInfo {
    Long userId;
    String email;
    String phoneNumber;
    String timezone;
    String language;
}
