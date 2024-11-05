package com.pratik.insta.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.User;
import com.pratik.insta.repository.UserRepository;
import com.pratik.insta.service.UserService;

@RestController
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signup")
	public ResponseEntity<User> registerUserHandler(@RequestBody User user) throws UserException {
		logger.info("Received signup request for user: {}", user.getUsername());

		User createdUser = userService.registerUser(user);
		logger.info("User registered successfully: {}", createdUser.getUsername());
		return new ResponseEntity<User>(createdUser, HttpStatus.OK);
	}

	@GetMapping("/signin")
	public ResponseEntity<User> signinHandler(Authentication auth) throws BadCredentialsException {
		logger.info("Received signin request for user: {}", auth.getName());

		 Optional<User>  opt = userRepository.findByEmail(auth.getName());

		if (opt.isPresent()) {
			
			logger.info("User logged in successfully: {}", opt.get().getUsername());
			return new ResponseEntity<User>(opt.get(), HttpStatus.OK);
		}
		logger.error("Invalid username or password");
		throw new BadCredentialsException("Invalid username or password");
	}
}
