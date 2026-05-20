package io.dispatcher.email.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;

import org.springframework.stereotype.Service;

import io.notification.common.configurations.RabbitMQEmailConfig;
import io.notification.common.enums.Priority;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailPriorityConsumer {

    private final SendGridEmailChannel sendGridEmailChannel;

    @RabbitListener(queues = RabbitMQEmailConfig.MAIN_EMAIL_QUEUE)
    public void consumeSmsMessages(MessageDetails messageDetails, Message rawMessage) {
        Priority messagePriority = messageDetails.getMessage().getPriority();
        System.out.println("Received SMS message: " + messageDetails);
        System.out.println("Rabbit priority: " + messagePriority);

        sendGridEmailChannel.sendMessage(
                messageDetails.getUserContactInfo(),
                messageDetails.getMessage().getMessageSubject(),
                messageDetails.getMessage().getMessageText());
    }
}
