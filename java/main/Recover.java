package main;
import data.Reset;
import data.Member;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
class Recover
{	
	@Autowired MemberRepository repository;
	@Autowired ResetRepository resetRepository;
	@Autowired EmailSettings settings;
	
	@GetMapping("/member-recover")
	String showRecoverPage(HttpSession session)
	{
		Member m = (Member)session.getAttribute("member");	
		return m == null ?
				"member-recover" :
				"redirect:/member-profile";
	}
	
	@PostMapping("/member-recover")
	String sendResetCode(String email, HttpSession session, Model model)
	{
		Member m = repository.findByEmail(email);
		if (m == null) {
			model.addAttribute("title",  "Recover Error");
			model.addAttribute("detail", "Unable to find your email.");
			return "display";
		}
		
		// check the account has been activated?
		
		Reset r = new Reset();
		r.member = m.code;
		r.secret = Tool.random(12);
		resetRepository.save(r);
		
		Email e = new Email(settings);
		e.sendResetCode(m.email, r.secret, r.code);
		model.addAttribute("title",  "Reset Code");
		model.addAttribute("detail", "The reset code has been sent to " +
										"your mailbox.");
		return "display";
	}
	
	@GetMapping("/member-recover-reset")
	String showResetPage(
				HttpSession session,
				String secret, 
				String code, 
				Model model)
	{	
		Member current = (Member)session.getAttribute("member");
		if (current != null) {
			return "redirect:/member-profile";
		}
		
		model.addAttribute("secret", secret);
		model.addAttribute("code", code);
		return "member-recover-reset";
	}
		
	@PostMapping("/member-recover-reset")
	String createNewPassword(
				HttpSession session,
				String password,
				String confirm,
				String key,
				int reset,
				Model model)
	{	
		Member current = (Member)session.getAttribute("member");
		if (current != null) {
			return "redirect:/member-profile";
		}
		
		if (password == null) password = "";
		if (confirm == null) confirm = "";
		if (key == null) key = "";
		
		Reset r = resetRepository.findBySecret(key);
		if (r == null || r.code != reset) {
			model.addAttribute("title",  "Reset Code");
			model.addAttribute("detail", "Unable to find your reset code.");			
			return "display";
		}
		
		
		boolean success = true;
		
		String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
		if (password.matches(pattern)) {
			// nothing
		} else {
			success = false;
		}
		
		if (password.equals(confirm)) {
			// nothing
		} else {
			success = true;
		}
		
		if (success) {
			resetRepository.deleteById(r.code);
			Member m = repository.findByCode(r.member);
			m.password = Tool.encrypt(password);
			repository.save(m);
			
			model.addAttribute("title",  "Success");
			model.addAttribute("detail", "Create new password successfully.");
			return "display";
		} else {
			model.addAttribute("title",  "Create New Password");
			model.addAttribute("detail", "The password confirmation does not " +
										"match.");
			return "display";
		}
	}
}
