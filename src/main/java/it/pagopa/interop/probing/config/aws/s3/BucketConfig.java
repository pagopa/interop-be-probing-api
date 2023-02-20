package it.pagopa.interop.probing.config.aws.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class BucketConfig {

	@Value("${amazon.clientS3.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.clientS3.secretkey}")
	private String amazonAWSSecretKey;
	
	@Value("${amazon.clientS3.region}")
	private String amazonAWSRegion;

	public AWSCredentials credentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey,amazonAWSSecretKey);
	}

	@Bean(name = "bucket-s3")
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials()))
				.withRegion(amazonAWSRegion)
				.build();
	}

}