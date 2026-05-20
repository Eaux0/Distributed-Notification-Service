package io.dispatcher.sms.service;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import io.notification.common.classes.NotificationChannel;
import jakarta.annotation.PostConstruct;

@Service
public class TwilioSmsChannel extends NotificationChannel {

    private String accountSid;
    private String authToken;
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public void sendMessage(String recipientPhoneNumber, String messageSubject, String messageText) {
        if (!this.isConnectionCorrect())
            throw new IllegalStateException(
                    "Missing Twilio account SID or auth token. Set twilio.account-sid or TWILIO_ACCOUNT_SID, twilio.auth-token or TWILIO_AUTH_TOKEN.");
        if (!this.isContactInfoPresent(recipientPhoneNumber))
            throw new IllegalStateException(
                    "Missing Twilio phone number. Set twilio.phone-number or TWILIO_PHONE_NUMBER.");
        try {
            Message.creator(
                    new PhoneNumber(recipientPhoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    messageText).create();

            System.out.println("SMS successfully sent via Twilio to " + recipientPhoneNumber);
        } catch (Exception e) {
            System.out.println("SMS failed to send via Twilio to " + recipientPhoneNumber);
            throw new RuntimeException("Twilio API failed to send SMS", e);
        }
    }

    @Override
    public Boolean isConnectionCorrect() {
        if (authToken == null || authToken.isBlank())
            return false;
        if (accountSid == null || accountSid.isBlank())
            return false;
        if (twilioPhoneNumber == null || twilioPhoneNumber.isBlank())
            return false;
        return true;
    }

    @Override
    public Boolean isContactInfoPresent(String userContactInfo) {
        return isPhoneNumberValid(userContactInfo);
    }
}
