package it.pagopa.interop.probing.probingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableFeignClients
@SpringBootApplication
@EnableAspectJAutoProxy
public class InteropProbingApplication {

  public static void main(String[] args) {
    SpringApplication.run(InteropProbingApplication.class, args);
  }

}
