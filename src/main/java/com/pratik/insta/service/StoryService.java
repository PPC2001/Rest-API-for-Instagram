package com.pratik.insta.service;

import java.util.List;

import com.pratik.insta.exception.StoryException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Story;

public interface StoryService {

	Story createStory(Story story, Integer userId) throws UserException;
	
	List<Story> findStoryByUserId(int userId) throws UserException , StoryException;
}
