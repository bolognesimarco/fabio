package com.bolo.photoshooters.web;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("SESSIONE CREATA");

	}
	
	
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		System.out.println("SESSIONE DISTRUTTA");
		HttpSession session = sessionEvent.getSession();
		UtenteBean utenteBean = (UtenteBean ) session.getAttribute("utenteBean");
		utenteBean.getUtente().setOnline(false);
		utenteBean.aggiornaProfilo();
		//metodo su utenteBean che setta hopme e refresh menùù
		utenteBean.sessioneDistrutta();
	}

}
