package main;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class Sample {
	
	@RequestMapping("/check") @ResponseBody
	String check() {
		return "OK";
	}

}

