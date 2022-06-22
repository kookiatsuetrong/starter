package main;
import java.security.MessageDigest;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class Start {
	public static void main(String[] data) {
		if (Common.running == false) {
			Common.context = SpringApplication.run(Initialize.class);
			Common.running = true;
		}
	}
}

@SpringBootApplication
class Initialize {
	
	@Bean
	DriverManagerDataSource createDataSource() {
		var source = new DriverManagerDataSource();
		source.setUrl(connectionString);
		return source;
	}
	
	@Value("${my.connection.string}")
	String connectionString;
}

class Common {
	
	static ApplicationContext context;
	static boolean running;
		
	static String encrypt(String s) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			byte[] data = digest.digest(s.getBytes());
			String t = "";
			for (byte b : data) {
				int k = b;
				if (k < 0) {
					k = 256 + k;
				}
				t += String.format("%02X", k);
			}
			return t;
		} catch (Exception e) { }
		return "Encryption Error";
	}
	
	static char[] pattern = "0123456789ABCEDF".toCharArray();
	static String random(int n) {
		String s = "";
		for (int i = 0; i < n; i++) {
			int index = (int)(Math.random() * pattern.length);
			s += pattern[index];
		}
		return s;
	}
}
