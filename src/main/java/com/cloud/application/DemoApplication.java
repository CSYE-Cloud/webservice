package com.cloud.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.cloud.application.repository.ImageRepository;
import com.cloud.application.repository.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cloud.application.*"})
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ImageRepository.class})
@EnableAutoConfiguration
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


}