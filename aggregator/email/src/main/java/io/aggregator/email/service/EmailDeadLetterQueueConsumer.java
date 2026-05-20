package io.aggregator.email.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.notification.common.configurations.RabbitMQEmailConfig;
import io.notification.common.model.DLQMessage;

@Service
public class EmailDeadLetterQueueConsumer {

    @RabbitListener(queues = RabbitMQEmailConfig.DLQ_EMAIL_QUEUE)
    public void consumeDeadLetter(DLQMessage dlqMessage) {
        System.out.println("Received message in Dead Letter Queue: " + dlqMessage);
    }
}
