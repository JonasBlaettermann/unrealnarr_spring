package com.sevenprinciples;

import com.sevenprinciples.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@Import({SecurityConfig.class })
public class SevenprinciplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SevenprinciplesApplication.class, args);
	}

}
