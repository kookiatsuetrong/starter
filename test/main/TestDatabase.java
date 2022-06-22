package main;

import data.Setting;

public class TestDatabase {

	public TestDatabase() {
		Start.main(null);
		repository = Common.context.getBean(SettingRepository.class);
	}
	
	SettingRepository repository;
	
	public void testSettings() {
		Iterable<Setting> result = repository.findAll();
		assert result != null;
	}
}
