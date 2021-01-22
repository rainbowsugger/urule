package com.rule.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication public class RuleClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuleClientApplication.class, args);
    }

}
