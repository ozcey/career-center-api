package com.careercenter.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.careercenter.model.User;
import com.careercenter.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with %s", username)));
		return UserPrincipal.create(user);
	}

}
