package com.frankester.savefiles;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SavefilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavefilesApplication.class, args);
	}

	@Bean
	public static AmazonS3 createS3Client(){
		return AmazonS3Client.builder()
				.standard().withRegion(Regions.US_EAST_1)
				.build();
	}
}
