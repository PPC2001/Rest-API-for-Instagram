package com.pratik.insta.service;

import java.util.List;

import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.User;

public interface UserService {

	User registerUser(User user) throws UserException;

	User findUserById(int id) throws UserException;

	User findUserProfile(String token) throws UserException;

	User findUserByUsername(String username) throws UserException;

	String followUser(int reqUserId, int followUserId) throws UserException;

	String unfollowUser(int reqUserId, int followUserId) throws UserException;

	List<User> findUserByIds(List<Integer> userIds) throws UserException;

	List<User> searchUser(String query) throws UserException;

	User updateUserDetails(User updatedUser, User existingUser) throws UserException;
}
