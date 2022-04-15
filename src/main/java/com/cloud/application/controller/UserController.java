package com.cloud.application.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import com.cloud.application.model.Image;
import com.cloud.application.model.User;
import com.cloud.application.model.request.UserUpdateRequest;
import com.cloud.application.model.response.UserRegistrationResponse;
import com.cloud.application.repository.ImageRepository;
import com.cloud.application.repository.UserRepository;
import com.cloud.application.service.SNS;
import com.cloud.application.service.Service;
import com.cloud.application.service.UserService;
import com.timgroup.statsd.StatsDClient;
import com.cloud.application.*;
import com.cloud.application.config.BadRequestException;
import com.cloud.application.config.ForbiddenException;

//@Component
@RestController
@RequestMapping("/v1")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UserService userService;
	
	@Autowired
	Service service;
	
	@Autowired
    private StatsDClient statsd;
	
	@Autowired
	ImageRepository imageRepo;
	
	@Autowired
	SNS snsService;
	
	private DynamoDB dynamoDB;

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public UserRegistrationResponse createUser(@RequestBody User user) {
		try {
            statsd.increment("Post user/ - Create new User");
            logger.info("in post user");
			if (user == null || user.getPassword() == null || user.getFirst_name() == null || user.getUsername() == null
					|| user.getLast_name() == null) {
				throw new BadRequestException();
			}

			Optional<User> u = userRepository.findByUsername(user.getUsername());

			System.out.println("checking if user is present");
			logger.info("checking if user is present");
			logger.info("user  ==== ", user.toString()); 
			if (u.isPresent()) {
				throw new BadRequestException();
			}

			// encrypt password
			String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);

			User entity = new User(user.getFirst_name(), user.getLast_name(), user.getPassword(), user.getUsername());
			User users = userRepository.save(entity);
			logger.info("user  ==== ", users.getUsername());
			//create entry in dynamodb to trigger lambda by sns
			snsService.postToTopic("POST", users.getUsername());

			UserRegistrationResponse userResponse = new UserRegistrationResponse();
			userResponse.setId(users.getId());
			userResponse.setFirstName(users.getFirst_name());
			userResponse.setLastName(users.getLast_name());
			userResponse.setUsername(users.getUsername());
			userResponse.setAccount_created(users.getAccountCreated());
			userResponse.setAccount_updated(users.getAccountUpdated());
			return userResponse;
		} catch (Exception e) {
			System.out.println("exception: " + e);
			throw new BadRequestException();
		}
	}

	@RequestMapping(value = "user/self", method = RequestMethod.GET)
	public UserRegistrationResponse displayUser(Authentication authentication, Principal principal) {
		
        statsd.increment("Get user/self - User");

		String name = principal.getName();
		User users = userService.loadUserByUsername(name);
		if(!users.isVerified()) {
			System.out.println("User is not yet verified");
//			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			throw new ForbiddenException();
		}
		
		UserRegistrationResponse userResponse = new UserRegistrationResponse();
		userResponse.setId(users.getId());
		userResponse.setFirstName(users.getFirst_name());
		userResponse.setLastName(users.getLast_name());
		userResponse.setUsername(users.getUsername());
		userResponse.setAccount_created(users.getAccountCreated());
		userResponse.setAccount_updated(users.getAccountUpdated());
//		return new ResponseEntity<>(userResponse, HttpStatus.OK);
		return userResponse;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "user/self", method = RequestMethod.PUT)
	public void updateUser(Authentication authentication, Principal principal, @RequestBody UserUpdateRequest request) {
		
		statsd.increment("Put user/self - Update User");
		
		userService.update(request, principal.getName());
	}
	
	 @PostMapping(value = "/user/self/pic")
	  public ResponseEntity<Image> createImage(@RequestParam(value="profilePic", required=true) 
	  MultipartFile profilePic, Authentication authentication, Principal principal)
			  throws Exception {
		 
		 String upd = principal.getName();
			if (upd == null || upd.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			statsd.increment("Post user/self/pic - Post pic of User");

			System.out.println("Setting for post request");
			
			Optional<User> tutorialData = userRepository.findByUsername(upd);

			Image img=null;
			if (tutorialData.isPresent()) {
				if(!tutorialData.get().isVerified()) {
					System.out.println("User is not yet verified");
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
								
					User user = tutorialData.get();
					 
					//check if already image i.e. update request
					Optional<Image> img1 = imageRepo.findByUserId(user.getId());
					if(img1.isPresent())
					{
						//delete
						String result = service.deleteFileFromS3Bucket(img1.get().getUrl(), user.getId());
				    	imageRepo.delete(img1.get());
					}

					String bucket_name =service.uploadFile( user.getId()+"/"+profilePic.getOriginalFilename(), profilePic);
					
					String url = bucket_name+"/"+ user.getId()+"/"+profilePic.getOriginalFilename(); 
					
				    img = new Image(profilePic.getOriginalFilename(), user.getId(), url);
				    imageRepo.save(img);
					
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}	
	    return new ResponseEntity<>(img, HttpStatus.CREATED);
	  }
	 
	 @GetMapping(value = "/user/self/pic")
	  public ResponseEntity<Image> getImage(Authentication authentication, Principal principal)
			  throws Exception {
				System.out.println("In get /user/self/pic");
				
		 //check user credentials and get userid
		 String upd = principal.getName();
			if (upd == null || upd.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			statsd.increment("Get user/self/pic - Get pic of User");

			System.out.println("Setting for get request");
			
			Optional<User> tutorialData = userRepository.findByUsername(upd);// AndPassword(userName, encodedPass);
			Optional<Image> img=null;
			if (tutorialData.isPresent()) {
				if(!tutorialData.get().isVerified()) {
					System.out.println("User is not yet verified");
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
				
					User user = tutorialData.get();
				    img = imageRepo.findByUserId(user.getId());
				    if (img.isPresent()) {
				  
				    	return new ResponseEntity<>(img.get(), HttpStatus.OK);
						  }
				    else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		 
	  }

		//delete image
	 @DeleteMapping(value = "/user/self/pic")
	  public ResponseEntity<String> deleteImage(Authentication authentication, Principal principal)
			  throws Exception {
				System.out.println("In delete /user/self/pic");
				statsd.increment("Delete user/self/pic - Delete pic of User");

		 String upd = principal.getName();
			if (upd == null || upd.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			Optional<User> tutorialData = userRepository.findByUsername(upd);// AndPassword(userName, encodedPass);
			Optional<Image> img=null;
			if (tutorialData.isPresent()) {
				if(!tutorialData.get().isVerified()) {
					System.out.println("User is not yet verified");
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
				
					User user = tutorialData.get();
										
				    img = imageRepo.findByUserId(user.getId());
				    
				    if (img.isPresent()) {
				    	
				    	String result = service.deleteFileFromS3Bucket(img.get().getUrl(),user.getId());
				    	imageRepo.delete(img.get());
				    	
				    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				    }
				    else {
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		 
	  }
	 
		 @GetMapping("/verifyUserEmail")
			public ResponseEntity<String> verifedUserUpdate(@RequestParam("email") String userEmail,
	                @RequestParam("token") String userToken) {
			 String result ="The user is not verified (get)";
				try {
					 AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
			        dynamoDB = new DynamoDB(client);	       
			        System.out.println("Get /verifyUserEmail");
			        Table userEmailsTable = dynamoDB.getTable("AccountDetails");
			        if(userEmailsTable == null) {
			            System.out.println("Table 'AccountDetails' is not in dynamoDB.");
			            return null;
			        }
			        
			        System.out.println("AccountDetails exits table");
			        System.out.println("AccountEmail in input is:"+userEmail);
					System.out.println("Index of spcae: in meial is: "+userEmail.indexOf(" ",0));
			        if(userEmail.indexOf(" ", 0)!=-1) {
						 userEmail=userEmail.replace(" ", "+");
					 }
			        System.out.println("EmailD after replacement is:"+userEmail);
			        //check if item exits
			        Item item = userEmailsTable.getItem("emailID",userEmail);
			        System.out.println("item= "+item);
			        if (item == null ) {
			        	result="token expired item not present";
			        }else {
			        	//if token expired
			        	BigDecimal tokentimeExpiry=(BigDecimal)item.get("TimeToLive");
			        	
			        	
			        	//calcuate now time
			        	long now = Instant.now().getEpochSecond(); // unix time
			            long timereminsa =  now - tokentimeExpiry.longValue(); // 2 mins in sec
			            System.out.println("tokentime: "+tokentimeExpiry);
			            System.out.println("now: "+now);
			            System.out.println("remins: "+timereminsa);
			            
			            
			            //ttl=(ttl + now); // when object will be expired
			        	if(timereminsa > 0)
			        	{
			        		//expired
			        		result="token expired";
			        	}

			        	 else {
			 				System.out.println("In get");
			 				result ="The user is verified (get)";
			 				//get user and update feilds
			 				
			 				saveFields(userEmail,  userToken);
			 		        }
			        }
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				
				return new ResponseEntity<>(result, HttpStatus.OK);
		 }
		 
//		 
//		 @PostMapping("/verifyUserEmail")
//			public ResponseEntity<String> verifedUserUpdatePost(@RequestParam("email") String email,
//	             @RequestParam("token") String token) {
//			 String result ="The user is not verified (get)";
//				try {
//					//System.out.println("in post");
//					//check if token is still valid
//					
//					System.out.println("In post");
//					result ="verified success post";
//					updateFields( email,  token);
//					
//				}
//				catch(Exception e)
//				{
//					System.out.println(e);
//				}
//				
//				return new ResponseEntity<>(result, HttpStatus.OK);
//		 }
//		 
		 public void saveFields(String rEmail, String randomToken) {
			 System.out.println("Email is: "+rEmail);
			 System.out.println("tokenis: "+randomToken);
			 
			 //check if email has space
			 if(rEmail.indexOf(' ', 0)!=-1) {
				 rEmail.replace(' ', '+');
			 }
			 
			 System.out.println("Now Email is: "+rEmail);
			 
			 Optional<User> tutorialData = userRepository.findByUsername(rEmail);
//			 if (tutorialData.isPresent()) {
				 
				 User u = tutorialData.get();
				 u.setVerified(true);
				 u.setVerified_on( OffsetDateTime.now(Clock.systemUTC()).toString());
				 u.setAccountUpdated(OffsetDateTime.now(Clock.systemUTC()).toLocalDateTime());
				 userRepository.save(u);
				 System.out.println("user fields save success");
//			 }
//			 else {
//				 System.out.println("error update verify user fields");
//			 }
			 
			 System.out.println("User updated with verify details");
		 }
		 
}
