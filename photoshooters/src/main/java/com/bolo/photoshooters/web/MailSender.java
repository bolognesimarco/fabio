package com.bolo.photoshooters.web;

import java.io.UnsupportedEncodingException;
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
 
        try {
			msg.setFrom(new InternetAddress(userName, "PHOTOSHOOTERs.NEt"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(message);
        // sends the e-mail
        Transport.send(msg);
    }
	
	private static String activationMessage = "Hai ricevuto questo messaggio perchè ti sei registrato al portale Photoshooters.net.\r\n"
			+ "Per completare la registrazione segui questo link: http://localhost:8080/activate?activationCode=%\r\n\r\n"
			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";
	
	public static void sendRegisterMail(String address, String activationCode) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", address, "Registrazione a Photoshooters.net", activationMessage.replace("%", activationCode));
	}	
	
	private static String cancellazioneUtenteNonAttivoMessage = "Utente:%, hai ricevuto questo messaggio perchè hai iniziato il processo di registrazione al portale Photoshooters.net senza completarlo entro 60gg.\r\n"
			+ "I dati inseriti sono stati cancellati. Per effettuare una nuova registrazione visita Photoshooters.net: http://www.photoshooters.net\r\n"
			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";
	
	public static void sendNonActivationMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", address, "Cancellazione registrazione - Photoshooters.net", cancellazioneUtenteNonAttivoMessage.replace("%", nomeUtente));
	}
	
	private static String nuovoMessaggioMessage = "Hai ricevuto un nuovo messaggio dal photoshooter %.\r\n"
			+ "Se non vuoi più ricevere una mail quando ricevi un nuovo messaggio, deseleziona l'opzione sul tuo profilo.";
	
	public static void sendNuovoMessaggioMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Nuovo messaggio - Photoshooters.net", nuovoMessaggioMessage.replace("%", nomeUtente));
	}
	
	private static String nuovoVotoMessage = "Hai ricevuto un voto ad una tua foto dal photoshooter %.\r\n"
			+ "Se non vuoi più ricevere una mail quando ricevi un nuovo voto, deseleziona l'opzione sul tuo profilo.";
	
	public static void sendNuovoVotoMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Nuovo voto - Photoshooters.net", nuovoVotoMessage.replace("%", nomeUtente));
	}
	
	private static String nuovoFollowerMessage = "Il photoshooter % ti segue!\r\n"
			+ "Se non vuoi più ricevere una mail quando ricevi un nuovo voto, deseleziona l'opzione sul tuo profilo.";
	
	public static void sendNuovoFollowerMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Nuovo follower - Photoshooters.net", nuovoFollowerMessage.replace("%", nomeUtente));
	}
	
	private static String nuovaFotoPreferitaMessage = "Il photoshooter % ha aggiunto una tua foto tra i preferiti.\r\n"
			+ "Se non vuoi più ricevere una mail quando ricevi un nuovo voto, deseleziona l'opzione sul tuo profilo.";
	
	public static void sendNuovaFotoPreferitaMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Foto preferita - Photoshooters.net", nuovaFotoPreferitaMessage.replace("%", nomeUtente));
	}
	
	private static String nuovoMessaggioInPostTuoMessage = "Il photoshooter % ha risposto ad un tuo post.\r\n"
			+ "Se non vuoi più ricevere una mail quando ricevi un nuovo voto, deseleziona l'opzione sul tuo profilo.";
	
	public static void sendNuovoMessaggioInPostTuoMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Post update - Photoshooters.net", nuovoMessaggioInPostTuoMessage.replace("%", nomeUtente));
	}
	
	private static String nuovaRispostaAnnuncioMessage = "Il photoshooter % ha risposto ad un tuo annuncio.\r\n"
			+ "Se non vuoi più ricevere una mail quando ricevi un nuovo voto, deseleziona l'opzione sul tuo profilo.";
	
	public static void sendNuovaRispostaAnnuncioMail(String address, String nomeUtente) throws Exception{
		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Annuncio update - Photoshooters.net", nuovaRispostaAnnuncioMessage.replace("%", nomeUtente));
	}
}
