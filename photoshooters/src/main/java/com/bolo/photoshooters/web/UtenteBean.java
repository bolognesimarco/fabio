package com.bolo.photoshooters.web;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;



@ManagedBean
@SessionScoped
public class UtenteBean {

	private Utente utente;
	
	private ServiziComuni serv = new ServiziComuniImpl();
	
	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	public Sesso[] getSessi() {
		return Sesso.values();
	}
	
	public void aggiornaProfilo() {
		
		try {

			serv.merge(utente);
			String mm = "PROFILo AGGIORNATo";
			contentBean.setMessaggio(mm);		
		} catch (Exception e) {
			e.printStackTrace();
	
		//	contentBean.setContent("profilo.xhtml");
			String mm = e.getMessage();
			contentBean.setMessaggio(mm);
		}
		
		//contentBean.setContent(null);
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
	}

	public ContentBean getContentBean() {
		return contentBean;
	}

	public void setContentBean(ContentBean contentBean) {
		this.contentBean = contentBean;
	}

	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;
}