package com.pratik.insta.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratik.insta.dto.UserDto;
import com.pratik.insta.exception.CommentException;
import com.pratik.insta.exception.PostException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Comment;
import com.pratik.insta.model.Post;
import com.pratik.insta.model.User;
import com.pratik.insta.repository.CommentRepository;
import com.pratik.insta.repository.PostRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Override
	public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException {
		User user = userService.findUserById(userId);
		Post post = postService.findPostById(postId);

		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		comment.setUser(userDto);
		comment.setCreatedAt(LocalDateTime.now());
		Comment createdComment = commentRepository.save(comment);
		post.getComments().add(createdComment);
		return createdComment;
	}

	@Override
	public Comment findCommentById(int commentId) throws CommentException {

		Optional<Comment> opt = commentRepository.findById(commentId);

		if (opt.isPresent()) {
			return opt.get();
		}

		throw new CommentException("Comment is not exist with id: " + commentId);
	}

	@Override
	public Comment likeComment(int commentId, int userId) throws CommentException, UserException {

		User user = userService.findUserById(userId);
		Comment comment = findCommentById(commentId);

		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		comment.getLikedByUsers().add(userDto);
		return commentRepository.save(comment);

	}

	@Override
	public Comment unlikeComment(int commentId, int userId) throws CommentException, UserException {
		User user = userService.findUserById(userId);
		Comment comment = findCommentById(commentId);

		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		comment.getLikedByUsers().remove(userDto);
		return commentRepository.save(comment);
	}

}
