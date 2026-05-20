package io.aggregator.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.notification.common.configurations.RabbitMQEmailConfig;

@SpringBootApplication
@Import(RabbitMQEmailConfig.class)
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}

}
