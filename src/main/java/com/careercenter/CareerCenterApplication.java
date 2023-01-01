package com.careercenter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class CareerCenterApplication {

	public static void main(String[] args) {

		SpringApplication.run(CareerCenterApplication.class, args);
	}

}
