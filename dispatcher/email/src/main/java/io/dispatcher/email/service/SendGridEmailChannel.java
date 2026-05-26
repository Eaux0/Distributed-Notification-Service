package io.dispatcher.email.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import io.notification.common.classes.NotificationChannel;

@Service
public class SendGridEmailChannel extends NotificationChannel {

    private static final String DEFAULT_SUBJECT = "Notification";

    @Value("${sendgrid.api-key}")
    private String apiKey;
    @Value("${sendgrid.from.email}")
    private String fromEmail;
    @Value("${sendgrid.from.name}")
    private String fromName;

    @Override
    public void sendMessage(String recipientEmail, String subject, String messageText) {
        if (!this.isConnectionCorrect())
            throw new IllegalStateException("Missing SendGrid API key. Set sendgrid.api-key or SENDGRID_API_KEY.");
        checkMessageStruture(recipientEmail, messageText);
        if (!this.isContactInfoPresent(this.fromEmail))
            throw new IllegalStateException(
                    "Bad Sender Email");
        try {
            Email from = new Email(fromEmail, fromName);
            Email to = new Email(recipientEmail);
            Content content = new Content("text/plain", messageText);
            Mail mail = new Mail(from, subject == null ? DEFAULT_SUBJECT : subject, to, content);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = new SendGrid(apiKey).api(request);
            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                throw new RuntimeException("SendGrid API returned status " + response.getStatusCode());
            }

            System.out.println("Email successfully sent via SendGrid to " + recipientEmail);
        } catch (IOException e) {
            System.out.println("Email failed to send via SendGrid to " + recipientEmail);
            throw new RuntimeException("SendGrid API failed to send email", e);
        }
    }

    @Override
    public Boolean isConnectionCorrect() {
        if (apiKey == null || apiKey.isBlank())
            return false;
        return true;
    }

    @Override
    public Boolean isContactInfoPresent(String userContactInfo) {
        return isEmailValid(userContactInfo);
    }
}
