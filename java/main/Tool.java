package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Base64;
import javax.imageio.ImageIO;

class Tool
{		
	static String encrypt(String s)
	{
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
	
	static String random(int n)
	{
		return random(n, "0123456789ABCEDF");
	}
	
	static String random(int n, String p)
	{
		char[] pattern = p.toCharArray();
		String s = "";
		for (int i = 0; i < n; i++) {
			int index = (int)(Math.random() * pattern.length);
			s += pattern[index];
		}
		return s;
	}
	
	static String getNumericRandom(int n)
	{
		return random(n, "0123456789");
	}
	
	static String createPhotoCode(String s)
	{
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
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", output);
			byte[] b = output.toByteArray();
			return Base64.getEncoder().encodeToString(b);
		} catch (Exception e) { }
		return "";
	}
}

