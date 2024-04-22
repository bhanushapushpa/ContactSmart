package com.springBoot.Bean.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springBoot.Bean.Entity.Contact;
import com.springBoot.Bean.Entity.UserData;


public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	@Query("From Contact as c where c.user.id =:userid")
	public Page<Contact> getContactByUserId(@Param("userid") int userid,Pageable pageable);
	
	
	public List<Contact> findByNameContainingAndUser(String name,UserData user);

}
