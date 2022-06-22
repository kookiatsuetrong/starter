package main;
import data.Member;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
class LogIn {
	
	@Autowired MemberRepository repository;
	
	@RequestMapping("/member-login")
	String showLogInPage(HttpSession session) {
		Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "member-login";
		} else {
			return "redirect:/member-profile";
		}
	}
	
	@PostMapping("/member-login")
	String checkPassword(
			String email, 
			String password,
			HttpSession session,
			Model model) {
		
		// TODO: Check session before continue
		
		Member m = repository.findByEmail(email);
		
		boolean success = false;
		String encrypted = Common.encrypt(password);
		
		if (m == null || m.password.equals(encrypted) == false) {
			model.addAttribute("title",  "Unable to log in");
			model.addAttribute("detail", "Invalid email or password. " +
								"Please click <a href='/member-login'>" +
								"here</a> to try again.");
		} else {
			switch (m.status) {
				case "registered":					
					model.addAttribute("title",  "Unable to log in");
					model.addAttribute("detail", "Please activate from your " +
												"email before continue.");
					break;
				default:
					success = true;
					session.setAttribute("member", m);
			}
		}
		
		if (success) {
			return "redirect:/member-profile";
		} else {
			return "display";
		}
	}
}
