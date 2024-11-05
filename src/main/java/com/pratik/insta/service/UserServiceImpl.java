package com.pratik.insta.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratik.insta.dto.UserDto;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.User;
import com.pratik.insta.repository.UserRepository;
import com.pratik.insta.security.JwtTokenClaims;
import com.pratik.insta.security.JwtTokenProvider;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public User registerUser(User user) throws UserException {
		Optional<User> isEmailExist = userRepository.findByEmail(user.getEmail());
		if (isEmailExist.isPresent()) {
			throw new UserException("Email is Already Exists");
		}

		Optional<User> isUsernameExist = userRepository.findByUsername(user.getUsername());
		if (isUsernameExist.isPresent()) {
			throw new UserException("Username is Already Taken...");
		}

		if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null
				|| user.getName() == null) {
			throw new UserException("All fields are required");
		}

		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setUsername(user.getUsername());
		newUser.setName(user.getName());
		newUser.setMobile(user.getMobile());
		newUser.setPassword(passwordEncoder.encode(user.getPassword())); // To encode the password

		logger.info("Encoded password: {}", newUser.getPassword());
		logger.info("Registering user: {}", newUser.getUsername());
		return userRepository.save(newUser);

	}

	@Override
	public User findUserById(int id) throws UserException {
		logger.info("Fetching user with id: {}", id);
		Optional<User> opt = userRepository.findById(id);
		if (opt.isPresent()) {
			User user = opt.get();
			logger.info("User found in database: {}", user);
			return user;
		}
		throw new UserException("User does not exist with id = " + id);
	}

	@Override
	public User findUserProfile(String token) throws UserException {
		// To remove the bearer from token
		token = token.substring(7);
		JwtTokenClaims jwtTokenClaims = jwtTokenProvider.getClaimsFromToken(token);
		String email = jwtTokenClaims.getUsername();
		Optional<User> opt = userRepository.findByEmail(email);
		if (opt.isPresent()) {
			logger.info("Finding user profile for token: {}", token);
			return opt.get();
		}
		throw new UserException("Invalid Token...");
	}

	@Override
	public User findUserByUsername(String username) throws UserException {
		logger.info("Fetching user with username: {}", username);

		Optional<User> opt = userRepository.findByUsername(username);
		if (opt.isPresent()) {
			logger.info("User found in database: {}", username);
			return opt.get();
		}
		throw new UserException("User not found with username: " + username);

	}

	@Override
	public String followUser(int reqUserId, int followUserId) throws UserException {

		User reqUser = findUserById(reqUserId);
		User followUser = findUserById(followUserId);

		UserDto follower = new UserDto();
		follower.setEmail(reqUser.getEmail());
		follower.setId(reqUser.getId());
		follower.setName(reqUser.getName());
		follower.setUserImage(reqUser.getUsername());
		follower.setUsername(reqUser.getUsername());

		UserDto following = new UserDto();
		following.setEmail(follower.getEmail());
		following.setId(follower.getId());
		following.setUserImage(follower.getUserImage());
		following.setName(follower.getName());
		following.setUsername(following.getUsername());

		reqUser.getFollowing().add(following);
		followUser.getFollower().add(follower);

		userRepository.save(followUser);
		userRepository.save(reqUser);

		logger.info("User {} is following user {}", reqUser.getUsername(), followUser.getUsername());
		return "You are following " + followUser.getUsername();
	}

	@Override
	public String unfollowUser(int reqUserId, int followUserId) throws UserException {
		User reqUser = findUserById(reqUserId);
		User followUser = findUserById(followUserId);

		UserDto follower = new UserDto();
		follower.setEmail(reqUser.getEmail());
		follower.setId(reqUser.getId());
		follower.setName(reqUser.getName());
		follower.setUserImage(reqUser.getUsername());
		follower.setUsername(reqUser.getUsername());

		UserDto following = new UserDto();
		following.setEmail(follower.getEmail());
		following.setId(follower.getId());
		following.setUserImage(follower.getUserImage());
		following.setName(follower.getName());
		following.setUsername(following.getUsername());

		reqUser.getFollowing().remove(following);
		followUser.getFollower().remove(follower);

		userRepository.save(followUser);
		userRepository.save(reqUser);

		logger.info("User {} has unfollowed user {}", reqUser.getUsername(), followUser.getUsername());
		return "You have unfollowed " + followUser.getUsername();
	}

	@Override
	public List<User> findUserByIds(List<Integer> userIds) throws UserException {
		List<User> users = userRepository.findAllUsersByUserIds(userIds);

		logger.info("Finding users by ids: {}", userIds);
		return users;
	}

	@Override
	public List<User> searchUser(String query) throws UserException {

		List<User> users = userRepository.findByQuery(query);

		if (users.size() == 0) {
			throw new UserException("User not found");
		}

		logger.info("Searching users by query: {}", query);
		return users;
	}

	@Override
	public User updateUserDetails(User updatedUser, User existingUser) throws UserException {
		if (!(updatedUser.getId() == existingUser.getId())) {
			throw new UserException("You can't update another user's profile");
		} else {
			if (updatedUser.getEmail() != null) {
				existingUser.setEmail(updatedUser.getEmail());
			}
			if (updatedUser.getUsername() != null) {
				existingUser.setUsername(updatedUser.getUsername());
			}
			if (updatedUser.getName() != null) {
				existingUser.setName(updatedUser.getName());
			}
			if (updatedUser.getPassword() != null) {
				existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
			}
			if (updatedUser.getMobile() != null) {
				existingUser.setMobile(updatedUser.getMobile());
			}
			if (updatedUser.getWebsite() != null) {
				existingUser.setWebsite(updatedUser.getWebsite());
			}
			if (updatedUser.getBio() != null) {
				existingUser.setBio(updatedUser.getBio());
			}
			if (updatedUser.getGender() != null) {
				existingUser.setGender(updatedUser.getGender());
			}
			if (updatedUser.getImage() != null) {
				existingUser.setImage(updatedUser.getImage());
			}
			logger.info("Updating user details for user: {}", existingUser.getUsername());
			return userRepository.save(existingUser);
		}
	}

}
