package com.bolo.photoshooters.service;

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
