package main;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

@Controller
class Web
{
	@RequestMapping("/")
	String showHome(HttpSession session, Model model)
	{
		Object o = session.getAttribute("member");
		model.addAttribute("member", o);
		return "/home";
	}
}

@Controller
class Error implements ErrorController
{
	@RequestMapping("/error")
	String show(HttpSession session, Model model)
	{
		Object o = session.getAttribute("member");
		model.addAttribute("member", o);
		
		model.addAttribute("title", "Not Found");
		model.addAttribute("detail", "Unable to find your request.");
		return "display";
	}
}
