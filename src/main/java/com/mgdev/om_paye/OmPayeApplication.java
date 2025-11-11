package com.mgdev.om_paye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class OmPayeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmPayeApplication.class, args);
	}

}
