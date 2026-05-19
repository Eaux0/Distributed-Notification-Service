package io.dispatcher.sms.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MAIN_QUEUE = "notification.sms.priority.queue";
    public static final String MAIN_EXCHANGE = "notification.sms.exchange";
    public static final String MAIN_ROUTING_KEY = "notification.sms";

    public static final String DLQ_QUEUE = "notification.sms.dlq";
    public static final String DLQ_EXCHANGE = "notification.sms.dlx";
    public static final String DLQ_ROUTING_KEY = "notification.sms.dlq";

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_QUEUE, true);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLQ_ROUTING_KEY);
    }

    @Bean
    public Queue priorityQueue() {
        // this is max heap - make enum - high = 2
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 2);

        args.put("x-dead-letter-exchange", DLQ_EXCHANGE);
        args.put("x-dead-letter-routing-key", DLQ_ROUTING_KEY);

        return new Queue(MAIN_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(priorityQueue()).to(mainExchange()).with(MAIN_ROUTING_KEY);
    }
}
