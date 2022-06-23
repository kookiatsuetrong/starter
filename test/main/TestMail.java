package main;
import java.util.Date;

public class TestMail {
	
	public void testSendSampleEmail() {
		// Start.main(null);
		
		EmailSender e = new EmailSender("beer@fullcodehub.com",
								"The Subject", 
								"The Main Content " + new Date() );
		e.start();
		try {
			Thread.sleep(10000);
		} catch (Exception x) { }
		assert e.success;
	}
}
