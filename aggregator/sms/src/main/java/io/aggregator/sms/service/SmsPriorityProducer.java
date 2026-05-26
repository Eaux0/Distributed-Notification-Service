package io.aggregator.sms.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import io.notification.common.classes.MessageStructureValidator;
import io.notification.common.configurations.RabbitMQSmsConfig;
import io.notification.common.enums.Channel;
import io.notification.common.model.MessageDetails;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SmsPriorityProducer {
    private static final String SMS_PRIORITY_TOPIC_PREFIX = "notification.sms.";

    public final KafkaTemplate<String, MessageDetails> kafkaTemplate;
    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = { "notification.sms.0", "notification.sms.1",
            "notification.sms.2" }, groupId = "notifications")
    public void smsPriorityListener(MessageDetails messageDetails, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        MessageStructureValidator.checkMessageDetailsStruture(messageDetails, Channel.SMS);
        System.out.println("Received Message: " + messageDetails.toString());

        final int priority = MessageStructureValidator.checkPriorityTopicStruture(topic, SMS_PRIORITY_TOPIC_PREFIX);

        rabbitTemplate.convertAndSend(RabbitMQSmsConfig.MAIN_SMS_EXCHANGE, RabbitMQSmsConfig.MAIN_SMS_ROUTING_KEY,
                messageDetails,
                message -> {
                    message.getMessageProperties().setPriority(priority);
                    return message;
                });
    }
}
