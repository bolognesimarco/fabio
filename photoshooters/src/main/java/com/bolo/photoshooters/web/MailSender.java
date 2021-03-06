package com.bolo.photoshooters.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.bolo.photo.web.entity.EmailDaInviare;

public class MailSender {
	public static void main(String[] aa) throws Exception{
		//MailSender rb = new MailSender();
		MailSender.sendEmail("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", "portoricano2000@gmail.com", "PROVA", "<html><body><b>Test</b><img src=\"c:\\temp\\avatar_fb.png\"/></body></html>" );
//		MailSender.sendEmail("smtp.gmail.com", "587", "portoricano2000@gmail.com", "modelletfcd", "portoricano2000@gmail.com", "PROVA", "<html><body><b>Test</b><img src=\"c:\\temp\\avatar_fb.png\"/></body></html>" );
		//RegisterMail("portoricano2000@gmail.com", "<html><body><b>Test</b></body></html>");
	}


	public static void sendEmail2(String host, String port,
            final String userName, final String password, EmailDaInviare email,
            String subject, String message) throws AddressException,
            MessagingException {
		email.setStatoLavorazione(1);
		String toAddress = email.getEmail();
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        //properties.put("mail.smtp.starttls.enable", "true");
 
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
        //msg.setContent(message, "text/html; charset=utf-8");
        //Text(message);
        
//        aggiungi image to mail
        MimeMultipart multipart = new MimeMultipart();       
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");
        // add it
        multipart.addBodyPart(messageBodyPart);
        
        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataSource fds = null;
        String pathURL = "http://localhost:8080";
		try {
			fds = new URLDataSource(new URL(pathURL+"/resources/images/logo_email.svg"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image>");
        messageBodyPart.setFileName("photoshooters.svg");
        // add image to the multipart
        multipart.addBodyPart(messageBodyPart);      
        msg.setContent(multipart);
        // sends the e-mail
        System.out.println("mail send primaaaaa");
        Transport.send(msg);
        System.out.println("mail send dopoooooo");
        email.setStatoLavorazione(2);
    }
	
//	tipoEmailDaInviare =11	
	private static String resetPasswordMessage = "Hai ricevuto questo messaggio perch� hai richiesto una nuova password per il portale Photoshooters.net.\r\n"
			+ "Per completare la procedura di reset password segui questo link: http://localhost:8080/password?activationCode=%\r\n\r\n"
			+ "Se non hai effettuato tu la richiesta semplicemente cancella questo messaggio, o avvisa l'amministrazione del portale.";	
	public static void sendResetPasswordMail(EmailDaInviare email, String activationCode) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", email, "Reset Password - Photoshooters.net", resetPasswordMessage.replace("%", activationCode));
	}	

//	tipoEmailDaInviare =9	
	private static String activationMessage = "Hai ricevuto questo messaggio perch� ti sei registrato al portale Photoshooters.net.\r\n"
			+ "Per completare la registrazione segui questo link: http://localhost:8080/activate?activationCode=%\r\n\r\n"
			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";	
	public static void sendRegisterMail(EmailDaInviare email, String activationCode) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", email, "Registrazione a Photoshooters.net", activationMessage.replace("%", activationCode));
	}	

//	tipoEmailDaInviare =10	
	private static String cancellazioneUtenteNonAttivoMessage = "Utente:%, hai ricevuto questo messaggio perch� hai iniziato il processo di registrazione al portale Photoshooters.net senza completarlo entro 60gg.\r\n"
			+ "I dati inseriti sono stati cancellati. Per effettuare una nuova registrazione visita Photoshooters.net: http://www.photoshooters.net\r\n"
			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";	
	public static void sendNonActivationMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", email, "Cancellazione registrazione - Photoshooters.net", cancellazioneUtenteNonAttivoMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =7
	private static String nuovoMessaggioMessage = "Hai ricevuto un nuovo messaggio dal photoshooter %.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando ricevi un nuovo messaggio, deseleziona l'opzione sul tuo profilo.";
	public static void sendNuovoMessaggioMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Nuovo messaggio - Photoshooters.net", nuovoMessaggioMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =8
	private static String nuovoVotoMessage = "Hai ricevuto un voto ad una tua foto dal photoshooter %.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando ricevi un nuovo voto, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovoVotoMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Nuovo voto - Photoshooters.net", nuovoVotoMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =5
	private static String nuovoFollowerMessage = "Il photoshooter % ti segue!\r\n"
			+ "Se non vuoi pi� ricevere una mail quando un nuovo photoshooter ti segue, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovoFollowerMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Nuovo follower - Photoshooters.net", nuovoFollowerMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =0
	private static String nuovaFotoPreferitaMessage = "Il photoshooter % ha aggiunto una tua foto tra i preferiti.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando un photoshooter aggiunge una tua foto tra i preferiti, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovaFotoPreferitaMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Foto preferita - Photoshooters.net", nuovaFotoPreferitaMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =6
	private static String nuovoMessaggioInPostTuoMessage = "Il photoshooter % ha risposto ad un tuo post.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta ad un tuo post, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovoMessaggioInPostTuoMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Post update - Photoshooters.net", nuovoMessaggioInPostTuoMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =3
	private static String nuovaRispostaInForumMessage = "Il photoshooter % ti ha risposto in un post del forum a cui hai partecipato.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta diretta a te nel forum, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovaRispostaInForumMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Risposta forum - Photoshooters.net", nuovaRispostaInForumMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =2
	private static String nuovaRispostaAnnuncioMessage = "Il photoshooter % ha risposto ad un tuo annuncio.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta ad un tuo annuncio o ad annunci a cui hai risposto, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovaRispostaAnnuncioMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Annuncio update - Photoshooters.net", nuovaRispostaAnnuncioMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =1
	private static String nuovaRispostaAnnuncioAltruiMessage = "Il photoshooter % ha risposto al tuo messaggio relativo all'annuncio da lui pubblicato.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta ad un tuo annuncio o ad annunci a cui hai risposto, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovaRispostaAnnuncioAltruiMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Annuncio update - Photoshooters.net", nuovaRispostaAnnuncioAltruiMessage.replace("%", nomeUtente));
	}
	
//	tipoEmailDaInviare =4
	private static String nuovoAlbumUtenteSeguitoMessage = "Il photoshooter % ha pubblicato un nuovo album.\r\n"
			+ "Se non vuoi pi� ricevere una mail quando un utente seguito pubblica un nuovo album, deseleziona l'opzione sul tuo profilo.";	
	public static void sendNuovoAlbumUtenteSeguitoMail(EmailDaInviare email, String nomeUtente) throws Exception{
		sendEmail2("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", email, "Nuovo album - Photoshooters.net", nuovoAlbumUtenteSeguitoMessage.replace("%", nomeUtente));
	}

	
//lasciato metodo per la proma sendEmail in main	
	public static void sendEmail(String host, String port,
  final String userName, final String password, String toAddress,
  String subject, String message) throws AddressException,
  MessagingException {

	// sets SMTP server properties
	Properties properties = new Properties();
	properties.put("mail.smtp.host", host);
	properties.put("mail.smtp.port", port);
	properties.put("mail.smtp.auth", "true");
	//properties.put("mail.smtp.starttls.enable", "true");
	
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
	//msg.setContent(message, "text/html; charset=utf-8");
	//Text(message);
	
	//aggiungi image to mail
	MimeMultipart multipart = new MimeMultipart();       
	BodyPart messageBodyPart = new MimeBodyPart();
	messageBodyPart.setContent(message, "text/html");
	// add it
	multipart.addBodyPart(messageBodyPart);
	
	// second part (the image)
	messageBodyPart = new MimeBodyPart();
	DataSource fds = null;
	String pathURL = "http://localhost:8080";
	try {
		fds = new URLDataSource(new URL(pathURL+"/resources/images/logo_email.svg"));
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	messageBodyPart.setDataHandler(new DataHandler(fds));
	messageBodyPart.setHeader("Content-ID", "<image>");
	messageBodyPart.setFileName("photoshooters.svg");
	// add image to the multipart
	multipart.addBodyPart(messageBodyPart);      
	msg.setContent(multipart);
	// sends the e-mail
	System.out.println("mail send primaaaaa");
	Transport.send(msg);
	System.out.println("mail send dopoooooo");
}
	
	
//	private static String activationMessage = "Hai ricevuto questo messaggio perch� ti sei registrato al portale Photoshooters.net.\r\n"
//			+ "Per completare la registrazione segui questo link: http://localhost:8080/activate?activationCode=%\r\n\r\n"
//			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";	
//	public static void sendRegisterMail(String address, String activationCode) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", address, "Registrazione a Photoshooters.net", activationMessage.replace("%", activationCode));
//	}	
//	
//	private static String cancellazioneUtenteNonAttivoMessage = "Utente:%, hai ricevuto questo messaggio perch� hai iniziato il processo di registrazione al portale Photoshooters.net senza completarlo entro 60gg.\r\n"
//			+ "I dati inseriti sono stati cancellati. Per effettuare una nuova registrazione visita Photoshooters.net: http://www.photoshooters.net\r\n"
//			+ "Se non hai effettuato tu la registrazione semplicemente cancella questo messaggio.";	
//	public static void sendNonActivationMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "register@photoshooters.net", "1Ochorios_", address, "Cancellazione registrazione - Photoshooters.net", cancellazioneUtenteNonAttivoMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =7
//	private static String nuovoMessaggioMessage = "Hai ricevuto un nuovo messaggio dal photoshooter %.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando ricevi un nuovo messaggio, deseleziona l'opzione sul tuo profilo.";
//	public static void sendNuovoMessaggioMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Nuovo messaggio - Photoshooters.net", nuovoMessaggioMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =8
//	private static String nuovoVotoMessage = "Hai ricevuto un voto ad una tua foto dal photoshooter %.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando ricevi un nuovo voto, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovoVotoMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Nuovo voto - Photoshooters.net", nuovoVotoMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =5
//	private static String nuovoFollowerMessage = "Il photoshooter % ti segue!\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando un nuovo photoshooter ti segue, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovoFollowerMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Nuovo follower - Photoshooters.net", nuovoFollowerMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =0
//	private static String nuovaFotoPreferitaMessage = "Il photoshooter % ha aggiunto una tua foto tra i preferiti.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando un photoshooter aggiunge una tua foto tra i preferiti, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovaFotoPreferitaMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Foto preferita - Photoshooters.net", nuovaFotoPreferitaMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =6
//	private static String nuovoMessaggioInPostTuoMessage = "Il photoshooter % ha risposto ad un tuo post.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta ad un tuo post, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovoMessaggioInPostTuoMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Post update - Photoshooters.net", nuovoMessaggioInPostTuoMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =3
//	private static String nuovaRispostaInForumMessage = "Il photoshooter % ti ha risposto in un post del forum a cui hai partecipato.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta diretta a te nel forum, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovaRispostaInForumMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Risposta forum - Photoshooters.net", nuovaRispostaInForumMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =2
//	private static String nuovaRispostaAnnuncioMessage = "Il photoshooter % ha risposto ad un tuo annuncio.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta ad un tuo annuncio o ad annunci a cui hai risposto, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovaRispostaAnnuncioMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Annuncio update - Photoshooters.net", nuovaRispostaAnnuncioMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =1
//	private static String nuovaRispostaAnnuncioAltruiMessage = "Il photoshooter % ha risposto al tuo messaggio relativo all'annuncio da lui pubblicato.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando c'� una nuova risposta ad un tuo annuncio o ad annunci a cui hai risposto, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovaRispostaAnnuncioAltruiMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Annuncio update - Photoshooters.net", nuovaRispostaAnnuncioAltruiMessage.replace("%", nomeUtente));
//	}
//	
////	tipoEmailDaInviare =4
//	private static String nuovoAlbumUtenteSeguitoMessage = "Il photoshooter % ha pubblicato un nuovo album.\r\n"
//			+ "Se non vuoi pi� ricevere una mail quando un utente seguito pubblica un nuovo album, deseleziona l'opzione sul tuo profilo.";	
//	public static void sendNuovoAlbumUtenteSeguitoMail(String address, String nomeUtente) throws Exception{
//		sendEmail("mail.photoshooters.net", "25", "info@photoshooters.net", "200AyAy=tera", address, "Nuovo album - Photoshooters.net", nuovoAlbumUtenteSeguitoMessage.replace("%", nomeUtente));
//	}
}
