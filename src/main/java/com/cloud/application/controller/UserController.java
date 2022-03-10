package com.cloud.application.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
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

import com.cloud.application.config.BadRequestException;
import com.cloud.application.model.Image;
import com.cloud.application.model.User;
import com.cloud.application.model.request.UserUpdateRequest;
import com.cloud.application.model.response.UserRegistrationResponse;
import com.cloud.application.model.response.UserUpdateResponse;
import com.cloud.application.repository.ImageRepository;
import com.cloud.application.repository.UserRepository;
import com.cloud.application.service.Service;
import com.cloud.application.service.UserService;

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
	ImageRepository imageRepo;

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public UserRegistrationResponse createUser(@RequestBody User user) {
		try {

			if (user == null || user.getPassword() == null || user.getFirst_name() == null || user.getUsername() == null
					|| user.getLast_name() == null) {
				throw new BadRequestException();
			}

			// check if already exists
			Optional<User> u = userRepository.findByUsername(user.getUsername());

			System.out.println("checking if user is present");
			if (u.isPresent()) {
				throw new BadRequestException();
			}

			// encrypt password
			String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);

			User entity = new User(user.getFirst_name(), user.getLast_name(), user.getPassword(), user.getUsername());
			User users = userRepository.save(entity);

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
		String name = principal.getName();
		User users = userService.loadUserByUsername(name);
		UserRegistrationResponse userResponse = new UserRegistrationResponse();
		userResponse.setId(users.getId());
		userResponse.setFirstName(users.getFirst_name());
		userResponse.setLastName(users.getLast_name());
		userResponse.setUsername(users.getUsername());
		userResponse.setAccount_created(users.getAccountCreated());
		userResponse.setAccount_updated(users.getAccountUpdated());
		return userResponse;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "user/self", method = RequestMethod.PUT)
	public void updateUser(Authentication authentication, Principal principal, @RequestBody UserUpdateRequest request) {
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

//			String pair = new String(Base64.decodeBase64(upd.substring(6)));
//			String userName = pair.split(":")[0];
//			String password = pair.split(":")[1];

			System.out.println("Setting for post request");
//			multiTenantManager.setCurrentTenant("all");
			
//			Optional<User> tutorialData = userRepository.findByUsername(userName);// AndPassword(userName, encodedPass);
			Optional<User> tutorialData = userRepository.findByUsername(upd);// AndPassword(userName, encodedPass);

			Image img=null;
			if (tutorialData.isPresent()) {

//				if (bCryptPasswordEncoder.matches(password, tutorialData.get().getPassword())) {

					//check if verified user
//					if(!tutorialData.get().isVerified()) {
//						System.out.println("User is not yet verified");
//						return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//					}
										
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
					
					//return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
//				} else {
//					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//				}
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

//			String pair = new String(Base64.decodeBase64(upd.substring(6)));
//			String userName = pair.split(":")[0];
//			String password = pair.split(":")[1];

			System.out.println("Setting for get request");
//			multiTenantManager.setCurrentTenant("get");
			
			Optional<User> tutorialData = userRepository.findByUsername(upd);// AndPassword(userName, encodedPass);
			Optional<Image> img=null;
			if (tutorialData.isPresent()) {

//				if (bCryptPasswordEncoder.matches(password, tutorialData.get().getPassword())) {

					//check if verified user
//					if(!tutorialData.get().isVerified()) {
//						System.out.println("User is not yet verified");
//						return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//					}
					
					User user = tutorialData.get();
				    img = imageRepo.findByUserId(user.getId());
//				    statsd.recordExecutionTime("DB Response Time - Image record get", System.currentTimeMillis() - startTime2);
				    if (img.isPresent()) {
				  
				    	return new ResponseEntity<>(img.get(), HttpStatus.OK);
						  }
				    else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}
//				} else {
//					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//				}
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		 
	  }

		//delete image
	 @DeleteMapping(value = "/user/self/pic")
	  public ResponseEntity<String> deleteImage(Authentication authentication, Principal principal)
			  throws Exception {
				System.out.println("In delete /user/self/pic");
		 //check user credentials and get userid
		 String upd = principal.getName();
			if (upd == null || upd.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

//			String pair = new String(Base64.decodeBase64(upd.substring(6)));
//			String userName = pair.split(":")[0];
//			String password = pair.split(":")[1];

			System.out.println("Setting for delete request");
//			multiTenantManager.setCurrentTenant("all");
			
			Optional<User> tutorialData = userRepository.findByUsername(upd);// AndPassword(userName, encodedPass);
			Optional<Image> img=null;
			if (tutorialData.isPresent()) {

//				if (bCryptPasswordEncoder.matches(password, tutorialData.get().getPassword())) {

					
					//check if verified user
//					if(!tutorialData.get().isVerified()) {
//						System.out.println("User is not yet verified");
//						return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//					}
//										
					User user = tutorialData.get();
										
				    img = imageRepo.findByUserId(user.getId());
				    
				    if (img.isPresent()) {
				    	
				    	String result = service.deleteFileFromS3Bucket(img.get().getUrl(),user.getId());
				    	imageRepo.delete(img.get());
				    	
				    	return new ResponseEntity<>(result, HttpStatus.OK);
				    }
				    else {
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
//				} else {
//					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//				}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		 
	  }
}
