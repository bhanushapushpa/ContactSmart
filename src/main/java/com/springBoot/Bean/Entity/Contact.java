package com.springBoot.Bean.Entity;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "Contact")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int contact_Id;
	
	@Column(name = "contact_name")
	@NotBlank(message = "Name Required")
	private String name;
	
	 @NotBlank(message = "Email Required") 
	private String email;
	
	 @NotBlank(message = "NickName Required") 
	private String nickName;
	
	
	private String profileImage;
	
	@Column(length = 5000)
	 @NotBlank(message = "Description Required") 
	private String description;
	
	 @NotBlank(message = "Work Required") 
	private String work;
	

	 @Pattern(regexp = "\\d{10}", message = "Required 10 digit Number") 
	private String phoneNumber;
	
	@ManyToOne
	@JsonIgnore
	private UserData user;
	
	
	public Contact() {
		super();
	}

	

	public Contact(int contact_Id, String name, String email, String nickName, String profileImage, String description,
			String work, String phoneNumber, UserData user) {
		super();
		this.contact_Id = contact_Id;
		this.name = name;
		this.email = email;
		this.nickName = nickName;
		this.profileImage = profileImage;
		this.description = description;
		this.work = work;
		this.phoneNumber = phoneNumber;
		this.user = user;
	}

	public int getContact_Id() {
		return contact_Id;
	}

	public void setContact_Id(int contact_Id) {
		this.contact_Id = contact_Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImage() {
		return profileImage;
	}

	public void setImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public UserData getUser() {
		return user;
	}

	public void setUser(UserData user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [contact_Id=" + contact_Id + ", contact_Name=" + name + ", email=" + email
				+ ", nickName=" + nickName + ", image=" + profileImage + ", description=" + description + ", work=" + work
				+ ", phoneNumber=" + phoneNumber + "]";
	}

	
	
	
	
}
