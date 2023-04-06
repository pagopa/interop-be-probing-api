package it.pagopa.interop.probing.probingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class InteropProbingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InteropProbingApplication.class, args);
	}

}
