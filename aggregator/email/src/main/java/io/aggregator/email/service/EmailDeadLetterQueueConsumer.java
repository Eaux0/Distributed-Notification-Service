package io.aggregator.email.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import io.notification.common.classes.RouteToBackupChannel;
import io.notification.common.configurations.RabbitMQEmailConfig;
import io.notification.common.model.MessageDetails;

@Service
public class EmailDeadLetterQueueConsumer {

    @Autowired
    RouteToBackupChannel routeToBackupChannel;

    @RabbitListener(queues = RabbitMQEmailConfig.DLQ_EMAIL_QUEUE)
    public void consumeDeadLetter(MessageDetails messageDetails, Message rawMessage) {
        System.out.println("Received message in Dead Letter Queue: " + messageDetails);
        System.out.println("DLQ headers: " + rawMessage.getMessageProperties().getHeaders());

        routeToBackupChannel.routeMessage(messageDetails.getMessage());
    }
}
