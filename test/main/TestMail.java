package main;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestMail {

	@Autowired EmailSettings settings;
		
	public void testSendSampleEmail() {
		EmailSender e = new EmailSender(settings,
								"beer@fullcodehub.com",
								"The Subject", 
								"The Main Content " + new Date() );
		e.start();
		try {
			Thread.sleep(10000);
		} catch (Exception x) { }
		assert e.success;
	}
}