package it.pagopa.interop.probing.probingapi.tracing.config;

import javax.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@Configuration
public class TracingConfig {

  @Bean
  public Filter tracingFilter() {
    return new AWSXRayServletFilter("Scorekeep");
  }

}
