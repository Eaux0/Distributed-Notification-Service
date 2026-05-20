package io.aggregator.sms.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.notification.common.configurations.RabbitMQSmsConfig;
import io.notification.common.model.DLQMessage;

@Service
public class SmsDeadLetterQueueConsumer {

    @RabbitListener(queues = RabbitMQSmsConfig.DLQ_SMS_QUEUE)
    public void consumeDeadLetter(DLQMessage dlqMessage) {
        System.out.println("Received message in Dead Letter Queue: " + dlqMessage);
    }

}
