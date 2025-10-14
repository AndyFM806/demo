package com.recortadorioBancario.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@EnableScheduling
@SpringBootApplication
public class RecordatorioBancarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordatorioBancarioApplication.class, args);
	}
}
