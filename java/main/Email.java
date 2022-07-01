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

@Service
class EmailSettings
{	
	String senderName     = "";
	String senderServer   = "";
	String senderPort     = "";
	String senderAddress  = "";
	String senderPassword = "";
	String baseDomain     = "";
	
	SettingRepository repository;
	 
	EmailSettings (SettingRepository repository)
	{
		this.repository = repository;
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
}

class Email
{
	Email(EmailSettings settings)
	{
		this.settings = settings;
	}
	
	EmailSettings settings;
	
	void sendActivationCode(
			String target,
			String secret,
			int code)
	{
		String content = "Welcome to " + settings.senderName + ". ";
		content += "Please click this link to activate your account. ";
		content += "<a href='" + settings.baseDomain + "/member-activate";
		content += "?secret=" + secret + "&code=" + code;
		content += "'>Activate Your Account</a>";
		
		EmailSender sender = new EmailSender(settings,
									target, "Member Activation", content);
		sender.start();
	}
	
	void sendResetCode(
			String target,
			String secret,
			int code)
	{
		String content = "";
		content += "Please click this link to reset your password. ";
		content += "<a href='" + settings.baseDomain + "/member-recover-reset";
		content += "?secret=" + secret + "&code=" + code;
		content += "'>Reset Your Password</a>";
		
		EmailSender sender = new EmailSender(settings,
									target, "Reset Password", content);
		sender.start();
	}
}

class EmailSender extends Thread
{	
	EmailSender(EmailSettings settings, String t, String s, String c)
	{
		this.settings = settings;
		target  = t;
		subject = s;
		content = c;
	}
	
	EmailSettings settings;
	
	String target;
	String subject;
	String content;
	
	boolean success = true;
	
	@Override public void run()
	{
		send();
	}
	
	void send()
	{	
		try {
			Properties p = new Properties();
			p.put("mail.smtp.auth", "true");
			p.put("mail.smtp.starttls.enable", "true");
			p.put("mail.smtp.host", settings.senderServer);
			p.put("mail.smtp.port", settings.senderPort);
			p.put("mail.smtp.ssl.protocols", "TLSv1.2");
			Session session = Session.getInstance(p, new Detail(settings));

			Message message = new MimeMessage(session);
			String sender = settings.senderName + "<" + 
							settings.senderAddress + ">";
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
		} catch (Exception x) {
			success = false;
		}
	}
}

class Detail extends Authenticator
{
	Detail(EmailSettings settings)
	{
		this.settings = settings;
	}
	
	EmailSettings settings;

	@Override protected PasswordAuthentication 
	getPasswordAuthentication()
	{
		return new PasswordAuthentication(
					settings.senderAddress,
					settings.senderPassword);
	}
}
