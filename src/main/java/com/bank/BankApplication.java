package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BankApplication {
    public static void main(String[] args) {
    	//this is the feature from branch feature-3
    	//this is just a comment to test the commit and push functionality of git
        SpringApplication.run(BankApplication.class, args);
    }
}
