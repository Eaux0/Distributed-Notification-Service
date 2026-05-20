package io.notification.common.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQEmailConfig {

    public static final String MAIN_EMAIL_QUEUE = "notification.email.priority.queue";
    public static final String MAIN_EMAIL_EXCHANGE = "notification.email.exchange";
    public static final String MAIN_EMAIL_ROUTING_KEY = "notification.email";

    public static final String DLQ_EMAIL_QUEUE = "notification.email.dlq";
    public static final String DLQ_EMAIL_EXCHANGE = "notification.email.dlx";
    public static final String DLQ_EMAIL_ROUTING_KEY = "notification.email.dlq";

    @Bean
    public Queue emailDeadLetterQueue() {
        return new Queue(DLQ_EMAIL_QUEUE, true);
    }

    @Bean
    public DirectExchange emailDeadLetterExchange() {
        return new DirectExchange(DLQ_EMAIL_EXCHANGE);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(emailDeadLetterQueue()).to(emailDeadLetterExchange()).with(DLQ_EMAIL_ROUTING_KEY);
    }

    @Bean
    public Queue priorityQueue() {
        // this is max heap - make enum - high = 2
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 2);

        args.put("x-dead-letter-exchange", DLQ_EMAIL_EXCHANGE);
        args.put("x-dead-letter-routing-key", DLQ_EMAIL_ROUTING_KEY);

        return new Queue(MAIN_EMAIL_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EMAIL_EXCHANGE);
    }

    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(priorityQueue()).to(mainExchange()).with(MAIN_EMAIL_ROUTING_KEY);
    }
}
