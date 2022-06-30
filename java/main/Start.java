package main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class Start {
	public static void main(String[] data) {
		context = SpringApplication.run(Initialize.class);
	}
	static ApplicationContext context;
}

@SpringBootApplication
class Initialize {
	
	@Bean
	DriverManagerDataSource createDataSource() {
		return new DriverManagerDataSource(connectionString);
	}
	
	@Value("${my.connection.string}")
	String connectionString;
}

class Common {
		
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
	
	static String random(int n) {
		return random(n, "0123456789ABCEDF");
	}
	
	static String random(int n, String p) {
		char[] pattern = p.toCharArray();
		String s = "";
		for (int i = 0; i < n; i++) {
			int index = (int)(Math.random() * pattern.length);
			s += pattern[index];
		}
		return s;
	}
	
	static String getNumericRandom(int n) {
		return random(n, "0123456789");
	}
	
	static String createPhotoCode(String s) {
		int width = 42;
		int height = 16;
		BufferedImage image = new BufferedImage(width, height,
									BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = image.createGraphics();
		g.setFont(new Font( Font.MONOSPACED, Font.PLAIN, 16) );
		g.setColor(new Color(32, 32, 32));
		g.drawString(s, 0, 14);
		g.dispose();
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", baos);
			byte[] b = baos.toByteArray();
			return Base64.getEncoder().encodeToString(b);
		} catch (Exception e) { }
		return "";
	}
}
