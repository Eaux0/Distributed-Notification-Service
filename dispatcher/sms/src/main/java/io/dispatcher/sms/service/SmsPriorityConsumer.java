package io.dispatcher.sms.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;

import org.springframework.stereotype.Service;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.configurations.RabbitMQSmsConfig;
import io.notification.common.enums.Channel;
import io.notification.common.enums.Priority;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SmsPriorityConsumer {

    private final TwilioSmsChannel twilioSmsChannel;

    @RabbitListener(queues = RabbitMQSmsConfig.MAIN_SMS_QUEUE)
    public void consumeSmsMessages(MessageDetails messageDetails, Message rawMessage) {
        MessageStructureValidator.checkMessageDetailsStruture(messageDetails, Channel.SMS);
        Priority messagePriority = messageDetails.getMessage().getPriority();
        System.out.println("Received SMS message: " + messageDetails);
        System.out.println("Rabbit priority: " + messagePriority);

        twilioSmsChannel.sendMessage(
                messageDetails.getUserContactInfo(),
                messageDetails.getMessage().getMessageSubject(),
                messageDetails.getMessage().getMessageText());
    }
}
