package com.rule.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RuleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RuleServerApplication.class, args);
	}

}
