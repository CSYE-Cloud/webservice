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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.application.config.BadRequestException;
import com.cloud.application.model.User;
import com.cloud.application.model.request.UserUpdateRequest;
import com.cloud.application.model.response.UserRegistrationResponse;
import com.cloud.application.model.response.UserUpdateResponse;
import com.cloud.application.repository.UserRepository;
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
	public UserRegistrationResponse register_user(Authentication authentication, Principal principal) {
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

}
