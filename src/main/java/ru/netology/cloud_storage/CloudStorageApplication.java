package ru.netology.cloud_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class CloudStorageApplication {
	public static void main(String[] args) {
		SpringApplication.run(CloudStorageApplication.class, args);
	}

}
