package com.prai.lineexpensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LineexpensetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LineexpensetrackerApplication.class, args);
	}

}
