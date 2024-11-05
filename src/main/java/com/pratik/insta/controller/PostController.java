package com.pratik.insta.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;

import com.pratik.insta.exception.PostException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Post;
import com.pratik.insta.model.User;
import com.pratik.insta.response.MessageResponse;
import com.pratik.insta.service.PostService;
import com.pratik.insta.service.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private static final Logger logger = LoggerFactory.getLogger(PostController.class);

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestHeader("Authorization") String token)
			throws UserException {
		logger.info("Received request to create post with token: {}", token);
		User user = userService.findUserProfile(token);
		Post createdPost = postService.createPost(post, user.getId());
		logger.info("Post created successfully for user ID {}: {}", user.getId(), createdPost);
		return new ResponseEntity<>(createdPost, HttpStatus.OK);
	}

	@GetMapping("/all/{userId}")
	public ResponseEntity<List<Post>> findPostByUserId(@PathVariable int userId) throws PostException {
		logger.info("Received request to find all posts for user ID: {}", userId);
		List<Post> posts = postService.findPostByUserId(userId);
		logger.info("Found posts for user ID {}: {}", userId, posts);
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@GetMapping("/following/{userIds}")
	public ResponseEntity<List<Post>> findAllPostByUserId(@PathVariable List<Integer> userIds)
			throws PostException, UserException {
		logger.info("Received request to find posts by following users with IDs: {}", userIds);
		List<Post> posts = postService.findAllPostByUserIds(userIds);
		logger.info("Found posts for following users with IDs {}: {}", userIds, posts);
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Post> findPostById(@PathVariable int postId) throws PostException {
		logger.info("Received request to find post by post ID: {}", postId);
		Post post = postService.findPostById(postId);
		logger.info("Found post by ID {}: {}", postId, post);
		return new ResponseEntity<>(post, HttpStatus.OK);
	}

	@PutMapping("/like/{postId}")
	public ResponseEntity<Post> likePost(@PathVariable int postId, @RequestHeader("Authorization") String token)
			throws UserException, PostException {
		logger.info("Received request to like post ID: {} by token: {}", postId, token);
		User user = userService.findUserProfile(token);
		Post likedPost = postService.likePost(postId, user.getId());
		logger.info("Post ID {} liked by user ID {}: {}", postId, user.getId(), likedPost);
		return new ResponseEntity<>(likedPost, HttpStatus.OK);
	}

	@PutMapping("/unlike/{postId}")
	public ResponseEntity<Post> unlikePost(@PathVariable int postId, @RequestHeader("Authorization") String token)
			throws UserException, PostException {
		logger.info("Received request to unlike post ID: {} by token: {}", postId, token);
		User user = userService.findUserProfile(token);
		Post unlikedPost = postService.unlikePost(postId, user.getId());
		logger.info("Post ID {} unliked by user ID {}: {}", postId, user.getId(), unlikedPost);
		return new ResponseEntity<>(unlikedPost, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{postId}")
	public ResponseEntity<MessageResponse> deletePost(@PathVariable int postId,
			@RequestHeader("Authorization") String token) throws PostException {
		logger.info("Received request to delete post ID: {} by token: {}", postId, token);
		User user = userService.findUserProfile(token);
		String message = postService.deletePost(postId, user.getId());
		logger.info("Post ID {} deleted by user ID {}: {}", postId, user.getId(), message);
		MessageResponse response = new MessageResponse(message);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PutMapping("/savePost/{postId}")
	public ResponseEntity<MessageResponse> savedPost(@RequestHeader("Authorization") String token,
			@PathVariable int postId) throws PostException, UserException {
		logger.info("Received request to save post ID: {} by token: {}", postId, token);
		User user = userService.findUserProfile(token);
		String message = postService.savedPost(postId, user.getId());
		logger.info("Post ID {} saved by user ID {}: {}", postId, user.getId(), message);
		MessageResponse response = new MessageResponse(message);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PutMapping("/unsavePost/{postId}")
	public ResponseEntity<MessageResponse> unSavedPost(@RequestHeader("Authorization") String token,
			@PathVariable int postId) throws PostException, UserException {
		logger.info("Received request to unsave post ID: {} by token: {}", postId, token);
		User user = userService.findUserProfile(token);
		String message = postService.unSavedPost(postId, user.getId());
		logger.info("Post ID {} unsaved by user ID {}: {}", postId, user.getId(), message);
		MessageResponse response = new MessageResponse(message);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

}
