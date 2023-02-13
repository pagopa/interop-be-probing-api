/*
 * 
 */
package it.pagopa.interop.probing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The Class InteropProbingApplication.
 */
@SpringBootApplication
@EnableScheduling
public class InteropProbingApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(InteropProbingApplication.class, args);
	}

}
