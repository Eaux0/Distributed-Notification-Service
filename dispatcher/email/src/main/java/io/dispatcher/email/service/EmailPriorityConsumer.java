package io.dispatcher.email.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;

import org.springframework.stereotype.Service;

import io.dispatcher.email.configurations.RabbitMQConfig;
import io.notification.common.enums.Priority;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailPriorityConsumer {

    private final SendGridEmailChannel sendGridEmailChannel;

    @RabbitListener(queues = RabbitMQConfig.MAIN_QUEUE)
    public void consumeSmsMessages(MessageDetails messageDetails, Message rawMessage) {
        Priority messagePriority = messageDetails.getMessage().getPriority();
        System.out.println("Received SMS message: " + messageDetails);
        System.out.println("Rabbit priority: " + messagePriority);

        sendGridEmailChannel.sendMessage(
                messageDetails.getUserContactInfo(),
                messageDetails.getMessage().getEvent().getMessageText());
    }
}
