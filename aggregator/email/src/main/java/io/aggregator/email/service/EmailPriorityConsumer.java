package io.aggregator.email.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import io.notification.common.configurations.RabbitMQEmailConfig;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailPriorityConsumer {
    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;
    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = { "notification.email.0", "notification.email.1",
            "notification.email.2" }, groupId = "notifications")
    public void emailPriorityListener(MessageDetails messageDetails,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("Received Message: " + messageDetails.toString());

        String lastChar = topic.substring(topic.length() - 1);
        final int priority = Integer.parseInt(lastChar);

        rabbitTemplate.convertAndSend(RabbitMQEmailConfig.MAIN_EMAIL_EXCHANGE,
                RabbitMQEmailConfig.MAIN_EMAIL_ROUTING_KEY,
                messageDetails,
                message -> {
                    message.getMessageProperties().setPriority(priority);
                    return message;
                });
    }
}
