package com.springBoot.Bean.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springBoot.Bean.Entity.Contact;
import com.springBoot.Bean.Entity.UserData;
import com.springBoot.Bean.Helper.Message;
import com.springBoot.Bean.Repository.ContactRepository;
import com.springBoot.Bean.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/* User controller */

	// Method for adding common data
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		String name = principal.getName();
		UserData user = userRepository.getUserByName(name);

		model.addAttribute("user", user);
	}

	// Home Page
	@GetMapping("/index")
	public String getUser(Model model, Principal principal) {
		model.addAttribute("title", "Home - Smart Contact Management");
		return "User/user";
	}

	// Add Contacts
	@GetMapping("/add-contact")
	public String openAddContact(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "User/add_contact_form";
	}

	// Add contacts Post Method
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
			@RequestParam("profileImage") MultipartFile file, HttpSession session, Principal principal, Model model) {

		try {
			if (result.hasErrors()) {
				model.addAttribute("contact", contact);
				return "User/add_contact_form";
			}

			model.addAttribute("title", "Add Contact");
			String name = principal.getName();
			UserData usOptional = userRepository.getUserByName(name);

			if (file.isEmpty()) {
				contact.setImage("contact.png");
			} else {
				contact.setImage(file.getOriginalFilename());
				File saveDir = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveDir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.createDirectories(path.getParent());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}

			contact.setUser(usOptional);

			usOptional.getContact().add(contact);
			userRepository.save(usOptional);

			session.setAttribute("message", new Message("Successfully Added", "alert-success"));
			return "User/add_contact_form";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.setAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));
			return "User/add_contact_form";
		}

	}

	/* View Page */
	@GetMapping("/show-contact/{page}")
	public String contacts(@PathVariable("page") Integer page, Model model, Principal principal) {

		model.addAttribute("title", "View Contact");
		// fetching User details from userRepository
		String name = principal.getName();
		UserData usOptional = userRepository.getUserByName(name);

		Pageable pageable = PageRequest.of(page, 5);

		// Fetching Contact List from contact Repository
		Page<Contact> contacts = contactRepository.getContactByUserId(usOptional.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("totalPages", contacts.getTotalPages());
		model.addAttribute("currentPages", page);
		return "User/viewContact";
	}

	/* Fetching individual Data */
	@GetMapping("{cid}/contact")
	public String indiviudalContact(@PathVariable("cid") Integer cid, Model model, Principal principal) {

		Optional<Contact> ContactOpt = contactRepository.findById(cid);
		Contact contact = ContactOpt.get();

		String name = principal.getName();
		UserData usOptional = userRepository.getUserByName(name);

		if (usOptional.getId() == contact.getUser().getId()) {
			model.addAttribute("model", contact);
			model.addAttribute("title", contact.getName());
		}

		return "User/individual_Contat";
	}

	/* Delete Contact */
	@GetMapping("/deleteContact/{cid}")
	public String delete(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session) {

		/* fetching contact from cid */
		Optional<Contact> ContactOpt = contactRepository.findById(cid);
		Contact contact = ContactOpt.get();

		String name = principal.getName();
		UserData usOptional = userRepository.getUserByName(name);

		if (usOptional.getId() == contact.getUser().getId()) {
			contact.setUser(null);
			contactRepository.delete(contact);
			session.setAttribute("message", new Message("Sucessfully Deleted", "alert-success"));
		} else {
			session.setAttribute("message", new Message("You don't have required permissions !!!", "alert-danger"));
		}

		return "redirect:/user/show-contact/0";
	}

	/* Update Contact */
	@PostMapping("/update-contact/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid, Model model) {

		Contact contact = contactRepository.findById(cid).get();

		model.addAttribute("title", "Update Contact");
		model.addAttribute("contact", contact);

		return "User/update-form";
	}

	/* Update Contact Processing */
	@PostMapping("/process-update")
	public String processContactUpdate(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
			@RequestParam("profileImage") MultipartFile file, HttpSession session, Principal principal, Model model) {

		Contact contactOld = contactRepository.findById(contact.getContact_Id()).get();

		try {
			if (!file.isEmpty()) {
				// deleting image
				File saveDir = new ClassPathResource("static/image").getFile();
				File file2 = new File(saveDir, contactOld.getImage());
				String img1 = contactOld.getImage();
				System.out.println(img1);
				if (!img1.equals("contact.png"))
					file2.delete();

				// saving new image
				Path path = Paths.get(saveDir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());

			} else {
				contact.setImage(contactOld.getImage());
			}

			UserData usOptional = userRepository.getUserByName(principal.getName());
			contact.setUser(usOptional);
			contactRepository.save(contact);
			session.setAttribute("message", new Message("Successfully Added", "alert-success"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "redirect:/user/" + contact.getContact_Id() + "/contact";

	}

	/* Profile */
	@GetMapping("/profile-check")
	public String profileCheck(Model model) {
		model.addAttribute("title", "Profile Page");
		return "User/prfille_check";
	}

	/* Setting */
	@GetMapping("/setting")
	public String settingSMC(Model model) {
		model.addAttribute("title", "Settings");
		return "User/setting";
	}

	/* Setting for change password */
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {

		UserData user = userRepository.getUserByName(principal.getName());

		if (this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			userRepository.save(user);
			session.setAttribute("message", new Message("Successfully Updated", "alert-success"));
		} else {
			session.setAttribute("message", new Message("Enter Wrong password", "alert-danger"));
			return "redirect:/user/setting";
		}

		return "redirect:/user/index";
	}

}
