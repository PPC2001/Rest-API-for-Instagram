package com.pratik.insta.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratik.insta.dto.UserDto;
import com.pratik.insta.exception.StoryException;
import com.pratik.insta.exception.UserException;
import com.pratik.insta.model.Story;
import com.pratik.insta.model.User;
import com.pratik.insta.repository.StoryRepository;
import com.pratik.insta.repository.UserRepository;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	private StoryRepository storyRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Story createStory(Story story, Integer userId) throws UserException {

		User user = userService.findUserById(userId);

		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		story.setUser(userDto);
		story.setTimestamp(LocalDateTime.now());

		user.getStories().add(story);

		return storyRepository.save(story);
	}

	@Override
	public List<Story> findStoryByUserId(int userId) throws UserException, StoryException {

		User user = userService.findUserById(userId);
		List<Story> stories = user.getStories();

		if (stories.size() == 0) {
			throw new StoryException("this user dosen't have any story");
		}

		return stories;
	}

}
