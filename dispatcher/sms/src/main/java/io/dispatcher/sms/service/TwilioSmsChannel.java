package io.dispatcher.sms.service;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class TwilioSmsChannel {

    private String accountSid;
    private String authToken;
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendMessage(String recipientPhoneNumber, String messageText) {
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
}
