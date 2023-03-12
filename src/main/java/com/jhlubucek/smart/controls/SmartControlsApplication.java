package com.jhlubucek.smart.controls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartControlsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartControlsApplication.class, args);
	}

}
