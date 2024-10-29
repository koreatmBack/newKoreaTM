package com.example.smsSpringTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmsSpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsSpringTestApplication.class, args);
	}

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

}
