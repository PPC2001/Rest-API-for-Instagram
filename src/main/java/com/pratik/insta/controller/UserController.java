package com.pratik.insta.controller;

import com.pratik.insta.model.User;
import com.pratik.insta.response.MessageResponse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.insta.exception.UserException;
import com.pratik.insta.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired(required = false)
	private UserService userService;

	@PutMapping("/req")
	public ResponseEntity<User> findUserProfile(@RequestHeader("Authorization") String token) throws UserException {
		logger.info("Received request to find user profile with token: {}", token);
		User user = userService.findUserProfile(token);
		logger.info("User profile found: {}", user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findUserById(@PathVariable int id) throws UserException {
		logger.info("Received request to find user by ID: {}", id);
		User user = userService.findUserById(id);
		logger.info("User found with ID {}: {}", id, user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<User> findUserByUsername(@PathVariable String username) throws UserException {
		logger.info("Received request to find user by username: {}", username);
		User user = userService.findUserByUsername(username);
		logger.info("User found with username {}: {}", username, user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<List<User>> findUserByUserIds(@PathVariable List<Integer> userId) throws UserException {
		logger.info("Received request to find users by IDs: {}", userId);
		List<User> users = userService.findUserByIds(userId);
		logger.info("Users found with IDs {}: {}", userId, users);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PutMapping("/follow/{followUserId}")
	public ResponseEntity<MessageResponse> followUser(@PathVariable int followUserId,
			@RequestHeader("Authorization") String token) throws UserException {
		logger.info("Received request to follow user with ID: {} using token: {}", followUserId, token);
		User user = userService.findUserProfile(token);
		String message = userService.followUser(user.getId(), followUserId);
		logger.info("User {} followed user {}: {}", user.getId(), followUserId, message);
		MessageResponse response = new MessageResponse(message);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/unfollow/{unfollowUserId}")
	public ResponseEntity<MessageResponse> unfollowUser(@PathVariable int unfollowUserId,
			@RequestHeader("Authorization") String token) throws UserException {
		logger.info("Received request to unfollow user with ID: {} using token: {}", unfollowUserId, token);
		User user = userService.findUserProfile(token);
		String message = userService.unfollowUser(user.getId(), unfollowUserId);
		logger.info("User {} unfollowed user {}: {}", user.getId(), unfollowUserId, message);
		MessageResponse response = new MessageResponse(message);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUser(String query) throws UserException {
		logger.info("Received request to search for users with query: {}", query);
		List<User> users = userService.searchUser(query);
		logger.info("Search results for query '{}': {}", query, users);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PutMapping("/account/edit")
	public ResponseEntity<User> updateUserDetails(@RequestHeader("Authorization") String token, @RequestBody User user)
			throws UserException {
		logger.info("Received request to update user details with token: {}", token);
		User reqUser = userService.findUserProfile(token);
		User updatedUser = userService.updateUserDetails(user, reqUser);
		logger.info("User details updated for user {}: {}", reqUser.getId(), updatedUser);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

}
