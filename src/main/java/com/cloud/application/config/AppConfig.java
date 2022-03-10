package com.cloud.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AppConfig {

	 @Value("${aws.access_key_id}")
     private String awsId;

     @Value("${aws.secret_access_key}")
     private String awsKey;
     
//	@Configuration
//	public class AwsS3ClientConfig {

	    @Value("${aws.s3.region}")
	    private String region;
	    
	    @Bean
		public PasswordEncoder encoder() {
			return new BCryptPasswordEncoder();
		}
		
//	    @Primary
//	    @Bean 
//	    public AmazonS3 s3client() {
//
//	    	BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsId, awsKey);
//	         AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
//	                 .withRegion(Regions.fromName(region))
//	                 .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//	                 .build();
//
////	        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
////	        .withRegion(region)
////	        .withCredentials(new InstanceProfileCredentialsProvider(false))
////	        .build();
//	        return amazonS3Client;
//	       
//	    }
//	}
}
