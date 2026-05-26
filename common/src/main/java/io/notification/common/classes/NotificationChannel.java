package io.notification.common.classes;

import io.notification.common.util.EmailUtil;
import io.notification.common.util.SmsUtil;

public abstract class NotificationChannel {
    public abstract void sendMessage(String userContactInfo, String subject, String message);

    protected abstract Boolean isConnectionCorrect();

    protected abstract Boolean isContactInfoPresent(String userContactInfo);

    protected boolean isEmailValid(String Email) {
        return EmailUtil.isValidEmail(Email);
    }

    protected boolean isPhoneNumberValid(String phoneNumber) {
        return SmsUtil.isValidPhoneNumber(phoneNumber);
    }

    protected void checkMessageStruture(String userContactInfo, String message) {
        if (!isContactInfoPresent(userContactInfo)) {
            throw new IllegalArgumentException("User contact info is missing or invalid");
        }
        if (!MessageStructureValidator.hasText(message)) {
            throw new IllegalArgumentException("Message text cannot be blank");
        }
    }
}
