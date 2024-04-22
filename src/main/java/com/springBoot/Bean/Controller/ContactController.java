package com.springBoot.Bean.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springBoot.Bean.Entity.UserData;
import com.springBoot.Bean.Helper.Message;
import com.springBoot.Bean.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ContactController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/* Home controller */
	@GetMapping("/")
	public String getHome(Model model) {
		model.addAttribute("title", "Home - Smart Contact Management");
		return "normal/home";
	}

		/* About controller */

	@GetMapping("/about")
	public String getAbout(Model model) {
		model.addAttribute("title", "About - Smart Contact Management");
		return "normal/about";
	}

	/* Signup controller */

	@GetMapping("/signup")
	public String getRegister(Model model) {
		model.addAttribute("title", "Register - Smart Contact Management");
		model.addAttribute("user", new UserData());
		return "normal/signup";
	}

	/* post Register process */

	@PostMapping("/process_register")
	public String registerUser(@Valid @ModelAttribute("user") UserData user, BindingResult rs,
			@RequestParam(value = "aggrement", defaultValue = "false") boolean aggrement, Model model,
			HttpSession session) {
		try {
			if (!aggrement) {
				throw new Exception("Check terms and condition.");
			}

			if (rs.hasErrors()) {
				model.addAttribute("user", user);
				return "normal/signup";
			}
			user.setRole("USER");
			user.setEnable(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.print(user);

			this.userRepository.save(user);

			model.addAttribute("user", new UserData());

			session.setAttribute("message", new Message("Successfully Added", "alert-success"));
			return "normal/signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));
			return "normal/signup";
		}

	}

	/* User controller */

	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("title", "SignIn - Smart Contact Management");
		return "normal/signIn";
	}

}
