package it.pagopa.interop.probing.probingapi.tracing.config;

import javax.servlet.Filter;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@Configuration
public class TracingConfig {

  @Bean
  public Filter tracingFilter() {
    return new AWSXRayServletFilter("interop-probing-apigw-dev/dev");
  }

  @Bean
  public HttpClientBuilder xrayHttpClientBuilder() {
    return HttpClientBuilder.create();
  }
}
