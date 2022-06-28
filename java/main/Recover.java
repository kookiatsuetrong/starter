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
class Recover {	
	@Autowired MemberRepository repository;
	@Autowired ResetRepository resetRepository;
	@Autowired EmailSettings settings;
	
	@GetMapping("/member-recover")
	String showRecoverPage(HttpSession session) {
		// TODO: Check session before continue
		Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "member-recover";
		} else {
			return "redirect:/member-profile";
		}
	}
	
	@PostMapping("/member-recover")
	String sendResetCode(String email, HttpSession session, Model model) {
		// TODO: Check session before continue
		
		Member m = repository.findByEmail(email);
		if (m == null) {
			model.addAttribute("title",  "Recover Error");
			model.addAttribute("detail", "Unable to find your email.");
			return "display";
		}
		
		Reset r = new Reset();
		r.member = m.code;
		r.secret = Common.random(12);
		resetRepository.save(r);
		
		Email e = new Email(settings);
		e.sendResetCode(m.email, r.secret, r.code);
		model.addAttribute("title",  "Reset Code");
		model.addAttribute("detail", "The reset code has been sent to " +
										"your mailbox.");
		return "display";
	}
	
	@GetMapping("/member-recover-reset")
	String showCreateNewPassword(String secret, String code, Model model) {
		// TODO: Check session before continue
		model.addAttribute("secret", secret);
		model.addAttribute("code", code);
		
		return "member-recover-reset";
	}
		
	@PostMapping("/member-recover-reset")
	String createNewPassword(
			String password,
			String confirm,
			String key,
			int reset,
			Model model) {
		
		// TODO: Check session before continue
		Reset r = resetRepository.findBySecret(key);
		if (r == null || r.code != reset) {
			model.addAttribute("title",  "Reset Code");
			model.addAttribute("detail", "Unable to find your reset code.");			
			return "display";
		}
		
		if (password.equals(confirm)) {
			resetRepository.deleteById(r.code);
			Member m = repository.findByCode(r.member);
			m.password = Common.encrypt(password);
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
