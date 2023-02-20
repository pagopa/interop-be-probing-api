package it.pagopa.interop.probing.config.aws.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

@Configuration
@Profile({"!dev", "!svil"})
public class SqsProdConfig {
	
	@Value("${amazon.sqs.region.static}")
	private String region;
	@Value("${amazon.sqs.end-point.services-queue}")
    private String sqsUrlServices;
	
	@Bean(name = "services")
    @Primary
    public AmazonSQSAsync amazonSQSAsync1() {
		return AmazonSQSAsyncClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsUrlServices, region))
				.build();
	}
	
	@Bean
	public QueueMessagingTemplate queueMessagingTemplate1() {
		return new QueueMessagingTemplate(amazonSQSAsync1());
	}
	

}
