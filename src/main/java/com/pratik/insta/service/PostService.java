package com.pratik.insta.service;

import java.util.List;

import com.pratik.insta.exception.PostException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Post;

public interface PostService {

	Post createPost(Post post , Integer userId) throws UserException;

	String deletePost(int postId, int userId) throws UserException, PostException;

	List<Post> findPostByUserId(Integer userid) throws UserException;

	Post findPostById(int postId) throws PostException;
	
	List<Post> findAllPostByUserIds(List<Integer> userIds)throws PostException, UserException;
	
	String savedPost(int postId , int userId) throws PostException , UserException;
	
	String unSavedPost(int postId , int userId) throws PostException , UserException;
	
	Post likePost(int PostId , int userId)throws UserException , PostException;
	
	Post unlikePost(int PostId , int userId)throws UserException , PostException;
	

}
