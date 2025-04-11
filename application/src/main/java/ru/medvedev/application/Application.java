package ru.medvedev.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("ru.medvedev.application.configuration")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
