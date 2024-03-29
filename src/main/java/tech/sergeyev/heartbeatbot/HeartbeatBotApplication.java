package tech.sergeyev.heartbeatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HeartbeatBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeartbeatBotApplication.class, args);
	}

}
