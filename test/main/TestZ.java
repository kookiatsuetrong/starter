package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TestZ {
	
	@Autowired ApplicationContext context;
	
	TestZ() {
		SpringApplication.exit(context);
	}
}