package com.server.dos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DosApplication {

	public static void main(String[] args) {
		SpringApplication.run(DosApplication.class, args);
	}

}
