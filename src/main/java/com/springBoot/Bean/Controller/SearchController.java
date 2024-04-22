package com.springBoot.Bean.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springBoot.Bean.Entity.Contact;
import com.springBoot.Bean.Entity.UserData;
import com.springBoot.Bean.Repository.ContactRepository;
import com.springBoot.Bean.Repository.UserRepository;

@RestController
public class SearchController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ContactRepository contactRepository;
	
	  @GetMapping("/search/{query}") public ResponseEntity<?>
	  searchBox(@PathVariable("query") String query,Principal principal){
	  
	  UserData userByName = userRepository.getUserByName(principal.getName());
	  
	  
	  List<Contact> contact =
	  contactRepository.findByNameContainingAndUser(query, userByName);
	  
	  return ResponseEntity.ok(contact);
	  
	  }
	 
}
