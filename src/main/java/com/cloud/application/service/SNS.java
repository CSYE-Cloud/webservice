package com.cloud.application.service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.time.Instant;


import java.util.Random;

@Service
public class SNS {
  
	SnsClient client;
    @Value("${aws.sns.topic.SNSTopic.ARN}")
    String snsTopic;
    
    private final static Logger logger = LoggerFactory.getLogger(SNS.class);
    public void postToTopic(String rEmail, String requestType) {

        try {
            Random token = new Random();
            int randomToken = token.nextInt(10000);
            String message = requestType + "|" + rEmail + "|" + randomToken;
            System.out.println("message generated, now publishing");
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(snsTopic)
                    .build();
            if (client == null) {
                System.out.println("client sns object is still null");
            }
            SnsClient snsClient = SnsClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
            PublishResponse publishResponse = snsClient.publish(request);
            System.out.println("Publishing done");
            System.out.println("Message " + publishResponse.messageId() + "is successfully published to SNS Topic 'SNSTopic'");


//            AmazonDynamoDB awsClient = AmazonDynamoDBClientBuilder.standard().build();
//            DynamoDB dynamo = new DynamoDB(awsClient);
//            Table table = dynamo.getTable("AccountDetails");
//            long now = Instant.now().getEpochSecond(); // unix time
//            long ttl = 120; // 2 mins for demo
//            Item item = new Item()
//                    .withPrimaryKey("emailID", rEmail)
//                    .with("token",randomToken)
//                    .with("TimeToLive",ttl + now);
//            PutItemOutcome outcome = table.putItem(item);
//            
            //logger.info("Message " + result.messageId() + " is successfully published to SNS Topic 'Notification_Email'.");
        } catch (SnsException e) {
            System.out.println("sns exception: " + e.getMessage());
            e.printStackTrace();
            logger.error("SNS Exception Warning - " + e.getMessage());
        }
    }
}

