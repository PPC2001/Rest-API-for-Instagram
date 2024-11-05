package com.pratik.insta.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.pratik.insta.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<com.pratik.insta.model.User> opt = userRepository.findByEmail(username);
		if (opt.isPresent()) {
			com.pratik.insta.model.User user = opt.get();
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Example authority
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					authorities);
		}
		logger.info("CustomUserDetailsService :: User not found with username " + username);
		throw new BadCredentialsException("User not found with username " + username);
	}

}
