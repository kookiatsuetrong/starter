package main;
import data.Member;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
class LogIs
{	
	@Autowired MemberRepository repository;
	
	@RequestMapping("/member-login")
	String showLogInPage(HttpSession session) 
	{
		Member m = (Member)session.getAttribute("member");
		return m == null ?	"member-login" :
							"redirect:/member-profile";
	}
	
	@PostMapping("/member-login")
	String checkPassword(
				HttpSession session,
				String email, 
				String password,
				Model model) 
	{	
		Member current = (Member)session.getAttribute("member");
		if (current != null) {
			return "redirect:/member-profile";
		}
				
		Member m = repository.findByEmail(email);
		
		boolean success = false;
		String encrypted = Tool.encrypt(password);
		
		if (m == null || m.password.equals(encrypted) == false) {
			model.addAttribute("title",  "Unable to log in");
			model.addAttribute("detail", "Invalid email or password. " +
								"Please click <a href='/member-login'>" +
								"here</a> to try again.");
		} else {
			// Without Activation
			// success = true;
			// session.setAttribute("member", m);
			
			switch (m.status) {
				case "registered":
					// model.addAttribute("title",  Message.LogInUnactivated);
					// model.addAttribute("detail", Message.LogInUnactivatedDetail);
					
					model.addAttribute("title",  "Unable to log in");
					model.addAttribute("detail", "Please activate from your " +
												"email before continue.");
					break;

				case "member":
				case "staff":
				case "administrator":
				default:
					success = true;
					session.setAttribute("member", m);
			}
		}
		
		return success ? "redirect:/member-profile" :
						 "display";
	}
	
}
