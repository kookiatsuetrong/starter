package main;
import data.Member;
import data.Activate;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
class Profile {
	
	@Autowired ActivateRepository activateRepository;
	@Autowired MemberRepository memberRepository;
	
	@RequestMapping("/member-activate")
	String activate(String secret, String code, Model model) {
		// TODO: Check session before
		boolean success = true;
		
		Activate t = activateRepository.findBySecret(secret);
		if (t == null) {
			success = false;
		} else {
			Member u = memberRepository.findByCode(t.member);
			if (u == null) {
				success = false;
			} else {
				u.status = "member";
				Member v = memberRepository.save(u);
				assert v.status.equals("member");
				activateRepository.deleteById(t.code);
			}
		}

		if (success) {
			model.addAttribute("title", "Activation Successfully");
			model.addAttribute("detail", "Your account has been activated.");
		} else {
			model.addAttribute("title", "Activation Error");
			model.addAttribute("detail", "Your account has not been activated.");
		}
		return "display";
	}
		
	@RequestMapping("/member-profile")
	String showProfilePage(HttpSession session, Model model) {
		Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/member-login";
		} else {
			model.addAttribute("member", m);
			return "member-profile";
		}
	}
	
	@RequestMapping("/member-logout")
	String showLogOutPage(HttpSession session, Model model) {
		session.removeAttribute("member");
		session.invalidate();
		model.addAttribute("title", "Logged Out");
		model.addAttribute("detail", "You have been logged out successfully.");
		return "display";
	}
}
