package com.pratik.insta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pratik.insta.exception.CommentException;
import com.pratik.insta.exception.PostException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Comment;
import com.pratik.insta.model.User;
import com.pratik.insta.service.CommentService;
import com.pratik.insta.service.UserService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;

	@PostMapping("/create/{postId}")
	public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @PathVariable int postId,
			@RequestHeader("Authorization") String token) throws UserException, PostException {
		User user = userService.findUserProfile(token);
		Comment createdComment = commentService.createComment(comment, postId, user.getId());
		logger.info("Comment created: {}", createdComment);
		return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
	}

	@PutMapping("/like/{commentId}")
	public ResponseEntity<Comment> likeComment(@RequestHeader("Authorization") String token,
			@PathVariable int commentId) throws UserException, CommentException {
		User user = userService.findUserProfile(token);
		Comment likedComment = commentService.likeComment(commentId, user.getId());
		logger.info("Comment liked: {}", likedComment);
		return new ResponseEntity<>(likedComment, HttpStatus.OK);
	}

	@PutMapping("/unlike/{commentId}")
	public ResponseEntity<Comment> unlikeComment(@RequestHeader("Authorization") String token,
			@PathVariable int commentId) throws UserException, CommentException {
		User user = userService.findUserProfile(token);
		Comment unlikedComment = commentService.unlikeComment(commentId, user.getId());
		logger.info("Comment unliked: {}", unlikedComment);
		return new ResponseEntity<>(unlikedComment, HttpStatus.OK);
	}
}
