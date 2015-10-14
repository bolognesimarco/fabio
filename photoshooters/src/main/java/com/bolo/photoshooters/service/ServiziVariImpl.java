package com.bolo.photoshooters.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;

import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.web.EMF;

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
		int giorniScadenza = 1;
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
		em.getTransaction().commit();
			return u;
		}else{
			return null;
		}
	}
	
	
	public Utente login(String username, String password) throws Exception{
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
		.createQuery("from Utente u where u.username=:user and u.password=:pass")
		.setParameter("user", username)
		.setParameter("pass", password)
		.getResultList();
		if(utenti!=null && utenti.size()>0){
			return utenti.get(0);
		}else{
			return null;
		}
	}

}
