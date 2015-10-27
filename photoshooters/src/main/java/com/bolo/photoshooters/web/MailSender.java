package com.bolo.photoshooters.web;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	public static void main(String[] aa) throws Exception{
		//MailSender rb = new MailSender();
		MailSender.sendRegisterMail("bolognesi.marco@gmail.com", "messaggioneeee");
	}
	
	private static String activationMessage = "Hai ricevuto questo messaggio perchè ti sei registrato al portale Photoshooters.net.\r\n"
			+ "Per completare la registrazione segui questo link: http://localhost:8080/activate?activationCode=%\r\n\r\n"
			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";
	
	public static void sendRegisterMail(String address, String activationCode) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", address, "Registrazione a Photoshooters.net", activationMessage.replace("%", activationCode));
	}
	
	public static void sendEmail(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String message) throws AddressException,
            MessagingException {
 
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(message);
 
        // sends the e-mail
        Transport.send(msg);
 
    }
	
	private static String cancellazioneUtenteNonAttivoMessage = "Utente:%, hai ricevuto questo messaggio perchè hai iniziato il processo di registrazione al portale Photoshooters.net senza completarlo entro 60gg.\r\n"
			+ "I dati inseriti sono stati cancellati. Per effettuare una nuova registrazione visita Photoshooters.net: http://www.photoshooters.net\r\n"
			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";
	
	public static void sendNonActivationMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", address, "Cancellazione registrazione a Photoshooters.net", cancellazioneUtenteNonAttivoMessage.replace("%", nomeUtente));
	}
}
