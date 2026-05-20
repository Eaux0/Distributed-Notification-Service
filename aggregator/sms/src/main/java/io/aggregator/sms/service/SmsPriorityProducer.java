package io.aggregator.sms.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import io.notification.common.configurations.RabbitMQSmsConfig;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SmsPriorityProducer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;
    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = { "notification.sms.0", "notification.sms.1",
            "notification.sms.2" }, groupId = "notifications")
    public void smsPriorityListener(MessageDetails messageDetails, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("Received Message: " + messageDetails.toString());

        String lastChar = topic.substring(topic.length() - 1);
        final int priority = Integer.parseInt(lastChar);

        rabbitTemplate.convertAndSend(RabbitMQSmsConfig.MAIN_SMS_EXCHANGE, RabbitMQSmsConfig.MAIN_SMS_ROUTING_KEY,
                messageDetails,
                message -> {
                    message.getMessageProperties().setPriority(priority);
                    return message;
                });
    }
}
