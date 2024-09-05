package com.unrealnarr;

import com.unrealnarr.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@Import({SecurityConfig.class })
public class UnrealnarrApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnrealnarrApplication.class, args);
	}

}
