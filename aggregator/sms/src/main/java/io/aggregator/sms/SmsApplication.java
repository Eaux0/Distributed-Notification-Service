package io.aggregator.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.notification.common.configurations.RabbitMQSmsConfig;

@SpringBootApplication
@Import(RabbitMQSmsConfig.class)
public class SmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);
	}

}
