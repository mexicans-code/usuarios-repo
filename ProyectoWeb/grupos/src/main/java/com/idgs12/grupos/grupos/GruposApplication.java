package com.idgs12.grupos.grupos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class GruposApplication {

	public static void main(String[] args) {
		SpringApplication.run(GruposApplication.class, args);
	}

}
