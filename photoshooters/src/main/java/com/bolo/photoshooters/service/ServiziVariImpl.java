package com.bolo.photoshooters.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;

import com.bolo.photo.web.entity.EmailDaInviare;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.web.EMF;
import com.bolo.photoshooters.web.MailSender;
import com.bolo.photoshooters.util.HashingSHAE;

public class ServiziVariImpl implements ServiziVari {

	@Override
	public boolean utenteEsiste(String username) throws Exception {
		EntityManager em = EMF.createEntityManager();
		return em
				.createQuery("select u.id from Utente u where u.username=:username")
				.setParameter("username", username)
				.getResultList()
				.size()>0;
	}
	

	
	@Override
	public boolean emailEsiste(String email) throws Exception {
		EntityManager em = EMF.createEntityManager();
		return em
				.createQuery("select u.id from Utente u where u.email=:email")
				.setParameter("email", email)
				.getResultList()
				.size()>0;
	}
	
	@Override
	public void cancellaUtentiNonAttivati() throws Exception{
		//dopo 30gg cancella utenti registrati ma non attivati
		
		Date currentDate = new Date(); // current date
        Calendar today = new GregorianCalendar();
		int giorniScadenza = 60;
        today.setTime(currentDate);				
		today.add(Calendar.DAY_OF_YEAR, -giorniScadenza);
        Date scadenza = today.getTime();

		System.out.println("DATE PrrOVa:::SCADENZA"+scadenza+":::TODAY"+today);
		final ServiziComuni serv = new ServiziComuniImpl();
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		List<Utente> utenti = em
				.createQuery("select u from Utente u where u.dataIscrizione < :scadenza and u.active = false")
				.setParameter("scadenza", scadenza)
				.getResultList();
				
		for (Utente ut : utenti) {
			EmailDaInviare email = new EmailDaInviare();
			email.setTipoEmail(10);
			email.setUtenteSender(ut);
			email.setEmail(ut.getEmail());
			serv.persist(email);
			MailSender.sendNonActivationMail(email, ut.getUsername());
			serv.delete(ut);
		}
				
	}
	
	@Override
	public boolean utenteAttivo(String email) throws Exception {
		EntityManager em = EMF.createEntityManager();
		return em
				.createQuery("select u.id from Utente u where u.email=:email and u.active = false")
				.setParameter("email", email)
				.getResultList()
				.size()>0;		
	}
	
	@Override
	public Utente activateUser(String activationCode) throws Exception{
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		List<Utente> utenti = em
			.createQuery("from Utente u where u.activationCode=:code")
			.setParameter("code", activationCode)
			.getResultList();
		if(utenti!=null && utenti.get(0)!=null){
			Utente u = utenti.get(0);
			System.out.println("============================================================"+u.getId());
			u.setActive(true);
			u.setAvatar("avatarDefault.svg");
			Date currentDate = new Date();
			u.setDataMember(currentDate);
			u.setDataUltimoAccesso(currentDate);
		em.getTransaction().commit();
			return u;
		}else{
			return null;
		}
	}
	
	@Override
	public Utente utenteConMailEsiste(String username, String email) throws Exception {
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
				.createQuery("from Utente u where u.username=:user and u.email=:mail")
				.setParameter("user", username)
				.setParameter("mail", email)
				.getResultList();
			if(utenti!=null && utenti.get(0)!=null){
				System.out.println("user="+utenti.get(0).getUsername()+" - email="+utenti.get(0).getEmail());
				return utenti.get(0);
			}else{
				return null;
			}
	}
	
	@Override
	public Utente resetPassword(String activationCode) throws Exception{
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		List<Utente> utenti = em
			.createQuery("from Utente u where u.activationCode=:code")
			.setParameter("code", activationCode)
			.getResultList();
		if(utenti!=null && utenti.get(0)!=null){
			Utente u = utenti.get(0);
			System.out.println("resetPassword servizio Vario - utente id= "+u.getId());

			Date currentDate = new Date();
			u.setDataUltimoAccesso(currentDate);
		em.getTransaction().commit();
			return u;
		}else{
			return null;
		}
	}
	
	
	public Utente login(String username, String password) throws Exception{
//		hashing password SHAE512
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
		.createQuery("from Utente u where u.username=:user")
		.setParameter("user", username)
//		.setParameter("pass", password)
		.getResultList();	
		if(utenti!=null && utenti.size()>0){
			String salt = utenti.get(0).getSalt();
			String hashedPassword = HashingSHAE.get_SHA_512_SecurePassword(password, salt);
			if (hashedPassword.equals(utenti.get(0).getPassword())) {
				return utenti.get(0);
			} else {
				System.out.println("user e/o password errati!");
				return null;			
			}
		}else{
			System.out.println("user non esistente!");
			return null;
		}
	}
	
	
	@Override
	public void invioEmailDaInviare() throws Exception {
		//dopo 30gg cancella utenti registrati ma non attivati
		
//		Date currentDate = new Date(); // current date
//        Calendar today = new GregorianCalendar();
////		int giorniScadenza = 60;
//		int secondiPassati = 30;
//        today.setTime(currentDate);				
//		today.add(Calendar.SECOND, secondiPassati);
//        Date scadenza = today.getTime();
//
//		System.out.println("INVIO EMAIL:::SCADENZA"+scadenza+":::TODAY"+today);
		final ServiziComuni serv = new ServiziComuniImpl();
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		List<EmailDaInviare> emailsDaInviare = em
				.createQuery("select e from EmailDaInviare e where e.statoLavorazione = 0")
//				.setParameter("scadenza", scadenza)
				.getResultList();
				
		for (EmailDaInviare email : emailsDaInviare) {
			
			switch (email.getTipoEmail()) {
			case 0:
				MailSender.sendNuovaFotoPreferitaMail(email, email.getUtenteSender().getUsername());
				break;
			case 1:
				MailSender.sendNuovaRispostaAnnuncioAltruiMail(email, email.getUtenteSender().getUsername());
				break;
			case 2:
				MailSender.sendNuovaRispostaAnnuncioMail(email, email.getUtenteSender().getUsername());
				break;
			case 3:
				MailSender.sendNuovaRispostaInForumMail(email, email.getUtenteSender().getUsername());
				break;
			case 4:
				MailSender.sendNuovoAlbumUtenteSeguitoMail(email, email.getUtenteSender().getUsername());
				break;
			case 5:
				MailSender.sendNuovoFollowerMail(email, email.getUtenteSender().getUsername());
				break;
			case 6:
				MailSender.sendNuovoMessaggioInPostTuoMail(email, email.getUtenteSender().getUsername());
				break;
			case 7:
				MailSender.sendNuovoMessaggioMail(email, email.getUtenteSender().getUsername());
				break;
			case 8:
				MailSender.sendNuovoVotoMail(email, email.getUtenteSender().getUsername());
				break;
			case 9:
				MailSender.sendRegisterMail(email, email.getUtenteSender().getActivationCode());
				break;
			case 10:
				MailSender.sendNonActivationMail(email, email.getUtenteSender().getUsername());
				break;
			case 11:
				MailSender.sendResetPasswordMail(email, email.getUtenteSender().getActivationCode());
				break;
			default:
				break;	
			}	
			if(email.getStatoLavorazione()==2) {
				serv.delete(email);
			}
		}

	}
	
}
