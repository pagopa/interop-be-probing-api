package it.pagopa.interop.probing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * The Class InteropProbingApplication.
 */
@SpringBootApplication
@EnableWebMvc
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
