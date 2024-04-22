package com.springBoot.Bean.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springBoot.Bean.Entity.UserData;


public interface UserRepository extends JpaRepository<UserData, Integer> {
	
	public Optional<UserData> getUserByEmail(String userName);
	
	public UserData getUserByName(String name);
	 

}
