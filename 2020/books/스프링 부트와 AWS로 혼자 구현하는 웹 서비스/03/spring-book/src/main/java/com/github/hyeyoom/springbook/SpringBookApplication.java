package com.github.hyeyoom.springbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBookApplication.class, args);
	}

}
