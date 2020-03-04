package com.sonim.bft.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = { "com.sonim" })
@RestController
public class RestAPIApplication {
	private static ApplicationContext ctx;

	public static void main(String[] args) {
		
		ctx = SpringApplication.run(RestAPIApplication.class, args);
	}

	public static void halt(String reason) {
		SpringApplication.exit(ctx);
	}
}
