package main;
import data.Member;
import data.Activate;
import java.time.Instant;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
class Register {
	
	@Autowired MemberRepository repository;
	@Autowired ActivateRepository activateRepository;
	
	@RequestMapping("/member-register")
	String showRegisterPage(HttpSession session) {
		Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "member-register";
		} else {
			return "redirect:/member-profile";
		}
	}
	
	@PostMapping("/member-register")
	String registerNewMember(
			String email,
			@RequestParam("first-name") String first,
			@RequestParam("family-name") String family,
			String password) {
		// member_register_invalid_email
		// member_register_invalid_first_name
		// member_register_invalid_last_name
		// member_register_invalid_password
		String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

		boolean p0 = email   .matches("^(.+)@(.+)$");
		boolean p1 = password.matches(pattern);
		boolean p2 = first   .matches("^.{2,20}$");
		boolean p3 = family  .matches("^.{2,20}$");
		
		boolean success = false;
		
		if (p0 && p1 && p2 && p3) {
			success = true;
		}

		if (success) {
			try {
				Member m     = new Member();
				m.email      = email;
				m.firstName  = first;
				m.familyName = family;
				m.password   = Common.encrypt(password);
				Member r     = repository.save(m);

				Activate a = new Activate();
				a.member   = r.code;
				a.secret   = Common.random(10);
				a.created  = Instant.now();
				activateRepository.save(a);

				Email e = new Email();
				e.sendActivationCode(m.email, a.secret, a.code);
			} catch (Exception e) {
				// member_register_duplicated_email
				success = false;
			}
		}
		
		if (success) {
			return "redirect:/member-register-success";
		}
		
		// TODO: Add error detail before return
		return "redirect:/member-register-error";
	}
	
	@RequestMapping("/member-register-success")
	String showSuccess(Model m) {
		m.addAttribute("title",  "Registration Successfully");
		m.addAttribute("detail", "Please go to your mailbox to activate " +
								 "your account.");
		return "display";
	}
	
	@RequestMapping("/member-register-error")
	String showError(Model m) {
		m.addAttribute("title",  "Registration Failed");
		m.addAttribute("detail", "Unable to register with your email.");
		return "display";
	}
}

/*
^                 # start-of-string
(?=.*[0-9])       # a digit must occur at least once
(?=.*[a-z])       # a lower case letter must occur at least once
(?=.*[A-Z])       # an upper case letter must occur at least once
(?=.*[!@#$%^&*])  # a special character must occur at least once
(?=\S+$)          # no whitespace allowed in the entire string
.{8,}             # anything, at least eight places though
$                 # end-of-string

Your password must contain:
At least one upper case English letter (i.e. A-Z)
At least one lower case English letter (i.e. a-z)
At least one digit (i.e. 0-9)
Minimum eight characters in length

// ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\S+$).{8,}$
String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

boolean p0 = email   .matches("^(.+)@(.+)$");
boolean p1 = password.matches(passwordPattern);
boolean p2 = first   .matches("^.{2,20}$");
boolean p3 = last    .matches("^.{2,20}$");

*/
