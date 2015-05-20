package com.bolo.photoshooters.web;


import java.util.Date;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.service.ServiziVari;
import com.bolo.photoshooters.service.ServiziVariImpl;

@ManagedBean
@SessionScoped
public class LoginBean {
	private String username;
	private String password;
	
	private ServiziComuni serv = new ServiziComuniImpl();

	public void login() {
		
		try {
			System.out.println("in login");
			Utente u = serviziVari.login(username, password);
			System.out.println("u:"+u);
			if(u!=null){
				String mm = "Completa il tuo profilo di photoshooter: inserisci";
				if(u.getDataNascita()==null){
					mm = mm+" anno di nascita -";
				}
				if(u.getSesso()==null){
					mm = mm+" genere -";
				}

				contentBean.setMessaggio(mm);				
				utenteBean.setUtente(u);
				contentBean.setContent("homePage.xhtml");
			}else{
				System.out.println("login ko");
				contentBean.setMessaggio("Utente e Password errati!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			contentBean.setContent("login.xhtml");
		}
		
		
	}
	
	private ServiziVari serviziVari = new ServiziVariImpl();

	public void checkUsername(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String usernameToCheck = (String) value;
		
		
		boolean esiste = false;
		try {
			esiste = serviziVari.utenteEsiste(usernameToCheck);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "GRAVE", "GRAVE"));
		}
		if (esiste) {
			String msg = "username già in uso";
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
		}
		
	}

	public void checkConfirmPassword(FacesContext context,
			UIComponent component, Object value) {

		String confirm = (String) value;
		String valuepassword = (String)((UIInput)context.getViewRoot().findComponent("registratiform:password")).getValue();
		
		if (valuepassword == null || !confirm.equals(valuepassword)) {
			String msg = "le password sono diverse";
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public ContentBean getContentBean() {
		return contentBean;
	}

	public void setContentBean(ContentBean contentBean) {
		this.contentBean = contentBean;
	}

	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;
	
	@ManagedProperty(value = "#{utenteBean}")
	private UtenteBean utenteBean;

	public UtenteBean getUtenteBean() {
		return utenteBean;
	}

	public void setUtenteBean(UtenteBean utenteBean) {
		this.utenteBean = utenteBean;
	}


}
