package io.aggregator.email.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.configurations.RabbitMQEmailConfig;
import io.notification.common.enums.Channel;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailPriorityConsumer {
    private static final String EMAIL_PRIORITY_TOPIC_PREFIX = "notification.email.";

    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;
    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = { "notification.email.0", "notification.email.1",
            "notification.email.2" }, groupId = "notifications")
    public void emailPriorityListener(MessageDetails messageDetails,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        MessageStructureValidator.checkMessageDetailsStruture(messageDetails, Channel.EMAIL);
        System.out.println("Received Message: " + messageDetails.toString());

        final int priority = MessageStructureValidator.checkPriorityTopicStruture(topic, EMAIL_PRIORITY_TOPIC_PREFIX);

        rabbitTemplate.convertAndSend(RabbitMQEmailConfig.MAIN_EMAIL_EXCHANGE,
                RabbitMQEmailConfig.MAIN_EMAIL_ROUTING_KEY,
                messageDetails,
                message -> {
                    message.getMessageProperties().setPriority(priority);
                    return message;
                });
    }
}
