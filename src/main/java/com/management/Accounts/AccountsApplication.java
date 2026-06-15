
package com.management.Accounts;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AccountsApplication {

	@Value("${spring.data.mongodb.uri:NOT_FOUND}")
	private String uri;

	@PostConstruct
	public void check() {
		System.out.println("Mongo URI = " + uri);
	}

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}
}