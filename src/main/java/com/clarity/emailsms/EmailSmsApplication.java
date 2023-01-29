package com.clarity.emailsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.clarity.emailsms")
public class EmailSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSmsApplication.class, args);
	}

}
