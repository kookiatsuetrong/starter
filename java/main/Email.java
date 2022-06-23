package main;
import data.Setting;
import java.util.Properties;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

class Email {	
	static String senderName = "";
	static String baseDomain = "";
	
	SettingRepository repository;
	
	void sendActivationCode(
			String target,
			String secret,
			int code) {
		repository = Start.context.getBean(SettingRepository.class);
		Iterable<Setting> all = repository.findAll();
		for (Setting s : all) {
			if ("base-domain"  .equals(s.name)) baseDomain = s.value;
			if ("platform-name".equals(s.name)) senderName = s.value;
		}
		String content = "Welcome to " + senderName + ". ";
		content += "Please click this link to activate your account. ";
		content += "<a href='" + baseDomain + "/member-activate";
		content += "?secret=" + secret + "&code=" + code;
		content += "'>Activate Your Account</a>";
		
		EmailSender sender = new EmailSender();
		sender.prepare(target, "Member Activation", content);
		sender.start();
	}
	
	void sendResetCode(
			String target,
			String secret,
			int code) {
		Iterable<Setting> all = repository.findAll();
		for (Setting s : all) {
			if ("base-domain"  .equals(s.name)) baseDomain = s.value;
			if ("platform-name".equals(s.name)) senderName = s.value;
		}
		String content = "";
		content += "Please click this link to reset your password. ";
		content += "<a href='" + baseDomain + "/member-recover-reset";
		content += "?secret=" + secret + "&code=" + code;
		content += "'>Reset Your Password</a>";
		
		EmailSender sender = new EmailSender();
		sender.prepare(target, "Reset Password", content);
		sender.start();
	}
}

class EmailSender extends Thread {
	SettingRepository repository;
	
	String senderName     = "";
	String senderServer   = "";
	String senderPort     = "";
	String senderAddress  = "";
	String senderPassword = "";
	String baseDomain     = "";
	 
	void prepare(String t, String s, String c) {
		target  = t;
		subject = s;
		content = c;
		
		repository = Start.context.getBean(SettingRepository.class);
		
		Iterable<Setting> all = repository.findAll();
		for (Setting e : all) {
			if ("base-domain"   .equals(e.name)) baseDomain     = e.value;
			if ("platform-name" .equals(e.name)) senderName     = e.value;
			if ("email-address" .equals(e.name)) senderAddress  = e.value;
			if ("email-password".equals(e.name)) senderPassword = e.value;
			if ("email-server"  .equals(e.name)) senderServer   = e.value;
			if ("email-port"    .equals(e.name)) senderPort     = e.value;
		}
	}
	
	String target;
	String subject;
	String content;
	
	boolean success = true;
	
	@Override public void run() {	
		send();
	}
	
	void send() {

		try {
			Properties p = new Properties();
			p.put("mail.smtp.auth", "true");
			p.put("mail.smtp.starttls.enable", "true");
			p.put("mail.smtp.host", senderServer);
			p.put("mail.smtp.port", senderPort);
			p.put("mail.smtp.ssl.protocols", "TLSv1.2");
			Session session = Session.getInstance(p, new Detail());

			Message message = new MimeMessage(session);
			String sender = senderName + "<" + 
							senderAddress + ">";
			message.setFrom(new InternetAddress(sender));
			message.setRecipients(
				Message.RecipientType.TO, 
				InternetAddress.parse(target));
			message.setSubject(subject);

			MimeBodyPart body = new MimeBodyPart();
			body.setContent(content, "text/html; charset=utf-8");
			Multipart part = new MimeMultipart();
			part.addBodyPart(body);

			message.setContent(part);
			Transport.send(message);
		} catch (Exception e) {
			System.out.println(e);
			success = false;
		}
	}
}

class Detail extends Authenticator {

	SettingRepository repository;
	String senderAddress  = "";
	String senderPassword = "";

	@Override protected PasswordAuthentication 
	getPasswordAuthentication() {
		repository = Start.context.getBean(SettingRepository.class);
		Iterable<Setting> all = repository.findAll();
		for (Setting e : all) {
			if ("email-address" .equals(e.name)) senderAddress  = e.value;
			if ("email-password".equals(e.name)) senderPassword = e.value;
		}
		return new PasswordAuthentication(
					senderAddress, senderPassword);
	}
}
