package com.spring.ecom.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.ecom.config.UserInfoConfig;
import com.spring.ecom.entities.User;
import com.spring.ecom.exceptions.ResourceNotFoundException;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private com.spring.ecom.repository.UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByEmail(username);
		
		return user.map(UserInfoConfig::new).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
	}
}