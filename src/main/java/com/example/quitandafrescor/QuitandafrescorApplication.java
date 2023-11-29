package com.example.quitandafrescor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class QuitandafrescorApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuitandafrescorApplication.class, args);
	}

}
