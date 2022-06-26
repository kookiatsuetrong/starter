package main;
import data.Setting;

public class TestDatabase {

	public TestDatabase() {
		repository = Start.context.getBean(SettingRepository.class);
	}
	
	SettingRepository repository;
	
	public void testSettings() {
		assert repository != null;
		Iterable<Setting> result = repository.findAll();
		assert result != null;
	}

}
