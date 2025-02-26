package com.miguel.pruebabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PruebaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaBackendApplication.class, args);
	}

}
