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
public class RabbitMQSmsConfig {

    public static final String MAIN_SMS_QUEUE = "notification.sms.priority.queue";
    public static final String MAIN_SMS_EXCHANGE = "notification.sms.exchange";
    public static final String MAIN_SMS_ROUTING_KEY = "notification.sms";

    public static final String DLQ_SMS_QUEUE = "notification.sms.dlq";
    public static final String DLQ_SMS_EXCHANGE = "notification.sms.dlx";
    public static final String DLQ_SMS_ROUTING_KEY = "notification.sms.dlq";

    @Bean
    public Queue smsDeadLetterQueue() {
        return new Queue(DLQ_SMS_QUEUE, true);
    }

    @Bean
    public DirectExchange smsDeadLetterExchange() {
        return new DirectExchange(DLQ_SMS_EXCHANGE);
    }

    @Bean
    public Binding smsDlqBinding() {
        return BindingBuilder.bind(smsDeadLetterQueue()).to(smsDeadLetterExchange()).with(DLQ_SMS_ROUTING_KEY);
    }

    @Bean
    public Queue priorityQueue() {
        // this is max heap - make enum - high = 2
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 2);

        args.put("x-dead-letter-exchange", DLQ_SMS_EXCHANGE);
        args.put("x-dead-letter-routing-key", DLQ_SMS_ROUTING_KEY);

        return new Queue(MAIN_SMS_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_SMS_EXCHANGE);
    }

    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(priorityQueue()).to(mainExchange()).with(MAIN_SMS_ROUTING_KEY);
    }
}
