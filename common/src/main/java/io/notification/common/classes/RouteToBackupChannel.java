package io.notification.common.classes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.notification.common.model.Message;

@Service
public class RouteToBackupChannel {

    @Autowired
    protected KafkaTemplate<String, Message> kafkaTemplate;

    public void routeMessage(Message message) {
        MessageStructureValidator.checkMessageStruture(message);
        String topic = "notification.raw";

        if (message.getEvent().getBackupChannel() == null) {
            return;
        }
        message.getEvent().setChannel(message.getEvent().getBackupChannel());
        message.getEvent().setBackupChannel(null);

        try {
            kafkaTemplate.send(topic, message).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while publishing to Kafka topic " + topic, exception);
        } catch (ExecutionException | TimeoutException exception) {
            throw new IllegalStateException("Failed to publish to Kafka topic " + topic, exception);
        }
    }

}
