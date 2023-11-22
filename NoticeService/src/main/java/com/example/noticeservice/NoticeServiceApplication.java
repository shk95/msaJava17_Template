package com.example.noticeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableJpaRepositories
@EnableFeignClients
@ConfigurationPropertiesScan("com.example.noticeservice.config")
@SpringBootApplication
public class NoticeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoticeServiceApplication.class, args);
	}

}
