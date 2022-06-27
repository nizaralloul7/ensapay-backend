package com.ensa.ENSAPAY;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEmailTools
public class EnsapayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnsapayApplication.class, args);
	}

}