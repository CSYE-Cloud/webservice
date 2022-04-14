package com.cloud.application.service;

//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.InstanceProfileCredentialsProvider;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.sns.AmazonSNS;
//import com.amazonaws.services.sns.AmazonSNSClientBuilder;
//import com.amazonaws.services.sns.model.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.regions.Region;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import java.util.Random;

@Service
public class SNS {
  //private AmazonSNS snsClient;
	
//	private SnsClient snsClient;
//	
//  private final static Logger logger = LoggerFactory.getLogger(SNS.class);
//  String topicArn;
//     
//  @Autowired
//  public SNS(){
//  //	 InstanceProfileCredentialsProvider mInstanceProfileCredentialsProvider = new InstanceProfileCredentialsProvider();
//  	//		 AWSCredentials credentials = mInstanceProfileCredentialsProvider.getCredentials();
//  			  //  InstanceProfileCredentialsProvider.createAsyncRefreshingProvider(true);
//  	 
//  	 //AKIA3RMFXDPBGRH6KRNY
//  	 //SyFOyiOrt5qbshSQbu01aX5TbMaWuO58BgkatYcn
//     // this.snsClient =  AmazonSNSClientBuilder.defaultClient();
//      		//.standard()
//      		//.withRegion("us-east-1")
//      		//.withCredentials((AWSCredentialsProvider) credentials)
//      		//.build();//defaultClient();
//
//  	SnsClient snsClient = SnsClient.builder()
//              .region(Region.US_EAST_1)                
//              .build();
//  	//"us-east-1"
//  	CreateTopicRequest request = CreateTopicRequest.builder()
//              .name("Notification_Email")
//              .build();
//
//  	CreateTopicResponse result = null;
//      result = snsClient.createTopic(request);
////      CreateTopicResult topic = snsClient.createTopic(
////              new CreateTopicRequest()
////                      .withName("Notification_Email"));
////      AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
////              .withRegion(region)
////              .withCredentials(new InstanceProfileCredentialsProvider(false))
////              .build();
//      topicArn = result.topicArn();
//      System.out.println("topicrn: "+topicArn);
//  }
//
//  public void postToTopic(String requestType, String recipientEmail){
//      try {
//      	System.out.println("in sns postToTopic");	
//      	Random rand = new Random(); //instance of random class
//          int upperbound = 2500;
//            //generate random values from 0-2499
//          int int_random = rand.nextInt(upperbound); 
//          String message1 = requestType + "|" + recipientEmail + "|" +
//                  "https://demo.garimachoudhary.me/v1/verifyUserEmail?email=" + recipientEmail + "&token="+ int_random
//                  + "|" + "messagetype-String" + "|" + int_random;
//          System.out.println("message generated, now publishing");	
//          PublishRequest publishReq = PublishRequest.builder()
//          		.message(message1)
//          		.topicArn(topicArn)
//          		.build();
//          if(publishReq == null)
//          {
//          	System.out.println("PublishRequest object is null");
//          }
//          System.out.println("PublishRequest done");
////          		(topicArn,
////                  requestType + "|" + recipientEmail + "|" +
////                  "http://prod.aditijain2025.me/v1/verifyUserEmail?email=" + recipientEmail + "&token="+ int_random
////                  + "|" + "messagetype-transactional");
//          
//          
//          if(snsClient == null)
//          {
//          	System.out.println("snsClient object is null");
//          	snsClient = SnsClient.builder()
//                      .region(Region.US_EAST_1)                
//                      .build();
//          	CreateTopicRequest request = CreateTopicRequest.builder()
//                      .name("Notification_Email")
//                      .build();
//
//          	CreateTopicResponse result = null;
//              result = snsClient.createTopic(request);
//              topicArn = result.topicArn();
//              System.out.println("topicrn: "+topicArn);
//          }
//          
//          if(snsClient == null)
//          {
//          	System.out.println("snsClient object is still   ..........null");
//          }
//          PublishResponse result = snsClient.publish(publishReq);
//          System.out.println("Publishing done");
//          System.out.println("Message "+ result.messageId() + "is successfully published to SNS Topic 'Notification_Email'");	
//          logger.info("Message "+ result.messageId() + " is successfully published to SNS Topic 'Notification_Email'.");
//      } catch (SnsException e) {
//      	System.out.println("sns exception: "+e.getMessage());
//      	 e.printStackTrace();
//      	
//          logger.error("SNS Exception Warning - " + e.getMessage());
//      }
//  }
	
	SnsClient snsClient;
    @Value("${aws.sns.topic.sample-a09-topic.ARN}")
    String snsTopicARN;
    private final static Logger logger = LoggerFactory.getLogger(SNS.class);
    public void postToTopic(String recipientEmail, String requestType) {

        try {
            System.out.println("in sns postToTopic");
            Random rand = new Random();
            int randomInt = rand.nextInt(10000);
            String snsMessage = requestType + "|" + recipientEmail + "|" +
                    "https://demo.mrigeshdasgupta.me/v1/verifyUserEmail?email=" + recipientEmail + "&token=" + randomInt
                    + "|" + "messagetype-String" + "|" + randomInt;
            System.out.println("message generated, now publishing");
            PublishRequest request = PublishRequest.builder()
                    .message(snsMessage)
                    .topicArn(snsTopicARN)
                    .build();
            if (snsClient == null) {
                System.out.println("snsClient object is still   ..........null");
            }
            SnsClient snsClient = SnsClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
            PublishResponse result = snsClient.publish(request);
            System.out.println("Publishing done");
            System.out.println("Message " + result.messageId() + "is successfully published to SNS Topic 'Notification_Email'");


            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
            DynamoDB dynamoDb = new DynamoDB(client);
            Table table = dynamoDb.getTable("ass");
//            PutItemRequest request = new PutItemRequest().withTableName("TokenTable").withReturnConsumedCapacity("TOTAL");
//            PutItemResult _response = client.putItem(request);
//            Map<String,Object> user_token = new HashMap<String, Object>();
//            user_token.put("emailID", recipientEmail);
//            user_token.put("expiration_time", (int_random));
            long now = Instant.now().getEpochSecond(); // unix time
            long ttl = 300; // 24 hours in sec
            Item item = new Item()
                    .withPrimaryKey("emailID", recipientEmail)
                    .with("Token",randomInt)
                    .with("TimeToLive",ttl + now);
            PutItemOutcome outcome = table.putItem(item);
            
            //logger.info("Message " + result.messageId() + " is successfully published to SNS Topic 'Notification_Email'.");
        } catch (SnsException e) {
            System.out.println("sns exception: " + e.getMessage());
            e.printStackTrace();
            logger.error("SNS Exception Warning - " + e.getMessage());
        }
    }
}

