package com.mst.actionms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ActionMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActionMsApplication.class, args);
	}

}
