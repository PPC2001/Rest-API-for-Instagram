package com.pratik.insta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratik.insta.dto.UserDto;
import com.pratik.insta.exception.PostException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Post;
import com.pratik.insta.model.User;
import com.pratik.insta.repository.PostRepository;
import com.pratik.insta.repository.UserRepository;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Post createPost(Post post, Integer userId) throws UserException {

		User user = userService.findUserById(userId);

		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		post.setUser(userDto);
		Post createdPost = postRepository.save(post);
		return createdPost;
	}

	@Override
	public String deletePost(int postId, int userId) throws UserException, PostException {

		Post post = findPostById(postId);
		User user = userService.findUserById(userId);

		if (post.getUser().getId() == (user.getId())) {
			postRepository.deleteById(post.getId());
			return "Post Deleted Successfully with id " + postId;
		}

		throw new PostException("You Can't delete other user's post");
	}

	@Override
	public List<Post> findPostByUserId(Integer userid) throws UserException {

		List<Post> posts = postRepository.findByUserId(userid);

		if (posts.size() == 0) {
			throw new UserException("this user does not have any post");
		}

		return posts;
	}

	@Override
	public Post findPostById(int postId) throws PostException {

		Optional<Post> opt = postRepository.findById(postId);

		if (opt.isPresent()) {
			return opt.get();
		}

		throw new PostException("Post not found with id - " + postId);
	}

	@Override
	public List<Post> findAllPostByUserIds(List<Integer> userIds) throws PostException, UserException {

		List<Post> posts = postRepository.findAllPostsByUserIds(userIds);

		if (posts.size() == 0) {
			throw new PostException("No Post available");
		}

		return posts;
	}

	@Override
	public String savedPost(int postId, int userId) throws PostException, UserException {

		Post post = findPostById(postId);
		User user = userService.findUserById(userId);

		if (!user.getSavedPost().contains(post)) {
			user.getSavedPost().add(post);
			userRepository.save(user);
		}

		return "Post Saved Successfully";
	}

	@Override
	public String unSavedPost(int postId, int userId) throws PostException, UserException {
		Post post = findPostById(postId);
		User user = userService.findUserById(userId);

		if (user.getSavedPost().contains(post)) {
			user.getSavedPost().remove(post);
			userRepository.save(user);
		}

		return "Post Remove Successfully";
	}

	@Override
	public Post likePost(int PostId, int userId) throws UserException, PostException {

		Post post = findPostById(PostId);
		User user = userService.findUserById(userId);

		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		post.getLikedByUsers().add(userDto);

		return postRepository.save(post);
	}

	@Override
	public Post unlikePost(int PostId, int userId) throws UserException, PostException {
		Post post = findPostById(PostId);
		User user = userService.findUserById(userId);

		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		post.getLikedByUsers().remove(userDto);

		return postRepository.save(post);
	}

}
