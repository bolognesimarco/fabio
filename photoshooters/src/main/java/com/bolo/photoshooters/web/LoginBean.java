package com.bolo.photoshooters.web;


import java.io.File;
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
	private ServiziVari serviziVari = new ServiziVariImpl();
	private ServiziComuni serv = new ServiziComuniImpl();

	public void login() {
		
		try {
			if (!username.isEmpty() || !password.isEmpty()) {

				System.out.println("in login");
				Utente u = serviziVari.login(username, password);
				System.out.println("dopo login - utente u:"+u);
				if(u!=null){
					String avatarDefault = "avatarDefault.svg";
					String mm = "";
					String mess = "completa il tuo profilo di photoshooter: inserisci";
					if(u.getDataNascita()==null){
						mm = mm+" data di nascita -";
					}
					if(u.getEsperienza()==null){
						mm = mm+" livello esperienza -";
					}
					if(u.getAvatar().equals(avatarDefault)){
						mm = mm+" avatar";
					}
					if(mm!=""){
						mm = mess+mm;
					}
					String mm2="BENVENUTo "+u.getName()+"\n"+mm;
					
					u.setOnline(true);
					Date currentDate = new Date();
					u.setDataUltimoAccesso(currentDate);
					serv.merge(u);
					utenteBean.setUtente(u);
					parametersBean.fillSelectItems();
					threadBean.nuoviMessaggiThread(u.getId());
					threadBean.cercaThreadsInviatiUtente(utenteBean.getUtente().getId());
					threadBean.cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
					inputBean.getVotoFoto().setScore(-1);
					contentBean.setMessaggio(mm2);
					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("pannellohomepagepanel");
					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
					contentBean.setContent("homePage.xhtml");
					
				}else{
					System.out.println("login ko");
					contentBean.setMessaggio("utente e/o password errati!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			contentBean.setContent("login.xhtml");
		}				
	}
	

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
	
//	****************GETTERS&SETTERS************
	
	
	
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

	@ManagedProperty(value = "#{parametersBean}")
	private ParametersBean parametersBean;
	
	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;
	
	@ManagedProperty(value = "#{utenteBean}")
	private UtenteBean utenteBean;

	@ManagedProperty(value = "#{threadBean}")
	private ThreadBean threadBean;
	
	@ManagedProperty(value = "#{inputBean}")
	private InputBean inputBean;
	
		
	public InputBean getInputBean() {
		return inputBean;
	}

	public void setInputBean(InputBean inputBean) {
		this.inputBean = inputBean;
	}

	public ThreadBean getThreadBean() {
		return threadBean;
	}

	public void setThreadBean(ThreadBean threadBean) {
		this.threadBean = threadBean;
	}

	public ParametersBean getParametersBean() {
		return parametersBean;
	}

	public void setParametersBean(ParametersBean parametersBean) {
		this.parametersBean = parametersBean;
	}

	public UtenteBean getUtenteBean() {
		return utenteBean;
	}

	public void setUtenteBean(UtenteBean utenteBean) {
		this.utenteBean = utenteBean;
	}


}
