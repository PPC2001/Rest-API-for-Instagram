package com.pratik.insta.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Story;
import com.pratik.insta.model.User;
import com.pratik.insta.service.StoryService;
import com.pratik.insta.service.UserService;

@RestController
@RequestMapping("/api/story")
public class StoryController {

	private static final Logger logger = LoggerFactory.getLogger(StoryController.class);

	@Autowired
	private StoryService storyService;

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public ResponseEntity<Story> createStory(@RequestBody Story story, @RequestHeader("Authorization") String token)
			throws UserException {
		logger.info("Received request to create story with token: {}", token);
		User user = userService.findUserProfile(token);
		Story createdStory = storyService.createStory(story, user.getId());
		logger.info("Story created successfully for user ID {}: {}", user.getId(), createdStory);
		return new ResponseEntity<>(createdStory, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<Story>> findAllStoryByUserId(@PathVariable int userId) throws UserException {
		logger.info("Received request to find all stories for user ID: {}", userId);
		List<Story> stories = storyService.findStoryByUserId(userId);
		logger.info("Stories found for user ID {}: {}", userId, stories);
		return new ResponseEntity<>(stories, HttpStatus.OK);
	}
}
