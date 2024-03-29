package com.cloud.application.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

@org.springframework.stereotype.Service
public class Service {

	    private Logger logger = LoggerFactory.getLogger(Service.class);

	    @Autowired
	    private AmazonS3 amazonS3Client;

	    @Value("${aws.s3.bucket}")
	    private String bucketName;

	    /**
	     * Upload file into AWS S3
	     *
	     * @param keyName
	     * @param file
	     * @return String
	     */
	    public String uploadFile(String keyName, MultipartFile file) {
	        try {
	            ObjectMetadata metadata = new ObjectMetadata();
	            metadata.setContentLength(file.getSize());
	            amazonS3Client.putObject(bucketName, keyName, file.getInputStream(), metadata);
	            return bucketName;
	        } catch (IOException ioe) {
	            logger.error("IOException: " + ioe.getMessage());
	        } catch (AmazonServiceException serviceException) {
	            logger.info("AmazonServiceException: "+ serviceException.getMessage());
	            throw serviceException;
	        } catch (AmazonClientException clientException) {
	            logger.info("AmazonClientException Message: " + clientException.getMessage());
	            throw clientException;
	        }
	        return "File not uploaded: " + keyName;
	    }

	    
	    public String deleteFileFromS3Bucket(String fileUrl, String userId) {
	        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
	        System.out.println("fileName to delete from service: "+bucketName + "/"+fileName);
	        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, userId+"/"+fileName));
	        return "Successfully deleted";
	    }
	
}
