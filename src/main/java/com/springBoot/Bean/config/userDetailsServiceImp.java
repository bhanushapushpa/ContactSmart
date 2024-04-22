package com.springBoot.Bean.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.springBoot.Bean.Entity.UserData;
import com.springBoot.Bean.Repository.UserRepository;

@Service
public class userDetailsServiceImp implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserData> usOptional = userRepository.getUserByEmail(username);
		if(usOptional.isPresent())
		{
			var userObj = usOptional.get();
			return User.builder()
					.username(userObj.getName())
					.password(userObj.getPassword())
					.roles(getRoles(userObj))
					.build();
		}
		else {
			throw new UsernameNotFoundException(username);
		}
		
		
		
	}
	

	private String[] getRoles(UserData userObj) {
		
		if(userObj.getRole() == null)
			return new String[] {"USER"};
		
		return userObj.getRole().split(",");
	}

}
