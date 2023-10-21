package main;
import data.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDatabase {
	
	@Autowired SettingRepository repository;
	
	public void testSettings() {
		assert repository != null;
		Iterable<Setting> result = repository.findAll();
		assert result != null;
	}
}
