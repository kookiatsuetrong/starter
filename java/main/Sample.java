package main;
import data.Setting;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
class Sample {
	
	@Autowired SettingRepository repository;
	
	@RequestMapping("/check") @ResponseBody
	String check() {
		return "OK";
	}
	
	@RequestMapping("/get-settings") @ResponseBody
	List<Setting> showSettings() {
		return (List<Setting>) repository.findAll();
	}
}

