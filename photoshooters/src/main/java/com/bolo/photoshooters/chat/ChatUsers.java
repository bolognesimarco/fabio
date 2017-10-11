package com.bolo.photoshooters.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.web.EMF;
 
@ManagedBean
@ApplicationScoped
public class ChatUsers implements Serializable {
     
	private static final long serialVersionUID = -6033657079964290753L;
	private List<String> users;
     
    @PostConstruct
    public void init() {
        users = new ArrayList<String>();
		EntityManager em = EMF.createEntityManager();
        List<Utente> utenti = em
				.createQuery("from Utente u where u.online = true")
//				.setParameter("user", username)
//				.setParameter("mail", email)
				.getResultList();
		if(utenti!=null && utenti.get(0)!=null){
	        for(Utente ut : utenti) {
	        	users.add(ut.getUsername());
	        }
		}

    }
 
    public List<String> getUsers() {
        return users;
    }
     
    public void remove(String user) {
        this.users.remove(user);
    }
     
    public void add(String user) {
        this.users.add(user);
    }
         
    public boolean contains(String user) {
        return this.users.contains(user);
    }
}