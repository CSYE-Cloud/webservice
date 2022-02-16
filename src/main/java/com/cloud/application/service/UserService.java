package com.cloud.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloud.application.config.BadRequestException;
import com.cloud.application.config.NoContentException;
import com.cloud.application.model.User;
import com.cloud.application.model.request.UserUpdateRequest;
import com.cloud.application.model.response.UserUpdateResponse;
import com.cloud.application.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<User> user = userRepository.findByUsername(username);
	    if (user.isEmpty()) {
	        throw new UsernameNotFoundException(username);
	    }
	    return user.get();
	}
	
	public UserUpdateResponse update(UserUpdateRequest request, String username) {
		if(!username.equals(request.getUsername()))
		{
			throw new BadRequestException();
		}
		Optional<User> opt = userRepository.findByUsername(username);
		User u = opt.get();
		
		u.setFirst_name(request.getFirstName());
		u.setLast_name(request.getLastName());
		String encryptedPassword = passwordEncoder.encode(request.getPassword());
		u.setPassword(encryptedPassword);
		
		userRepository.save(u);
		
		UserUpdateResponse response = new UserUpdateResponse();
		response.setFirstName(request.getFirstName());
		response.setLastName(request.getLastName());
		response.setUsername(username);
		
		 return response;
	}
	
	
}
