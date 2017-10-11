package com.bolo.photoshooters.web;


import java.io.File;
import java.security.NoSuchAlgorithmException;
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

import com.bolo.photo.web.entity.EmailDaInviare;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.service.ServiziVari;
import com.bolo.photoshooters.service.ServiziVariImpl;
import com.bolo.photoshooters.util.HashingSHAE;

@ManagedBean
@SessionScoped
public class LoginBean {
	private String username;
	private String password;
	private String emailPsw;
	private String usernameResetPsw;
	private String passwordReset;
	private String pswConfirmReset;
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
					threadBean.cercaThreadsInviatiUtente(utenteBean.getUtente().getId());
					threadBean.cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
					threadBean.nuoviMessaggiThread(u.getId());
					System.out.println("loginbean....start annunciiiiiiiiiii");
					annuncioBean.cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
					annuncioBean.cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
					annuncioBean.nuoviMessaggiThreadsAnnuncio(u.getId());
					postBean.nuoveRisposteForum(u);
					inputBean.getVotoFoto().setScore(-1);
					contentBean.setMessaggio(mm2);
					contentBean.setContent("homePage.xhtml");
					
				}else{
					System.out.println("login ko");
					contentBean.setContent("login.xhtml");
					contentBean.setMessaggio("utente e/o password errati!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			contentBean.setContent("login.xhtml");
		}				
	}
	
	
	public void caricaRecuperaPassword() {
		contentBean.setMessaggio("Inserisci username ed email: verrà inviata la procedura per creare una nuova password!");
		contentBean.setContent("recuperoPassword.xhtml");
	}	
	

	public void resettaPassword(String user, String email) {
		Utente esiste = null;
		try {
			esiste = serviziVari.utenteConMailEsiste(user, email);
			esiste.setActivationCode(UUID.randomUUID().toString());
			EmailDaInviare emailActivation = new EmailDaInviare();
			emailActivation.setTipoEmail(11);
			emailActivation.setUtenteSender(esiste);
			emailActivation.setEmail(email);
			serv.merge(esiste);
			serv.persist(emailActivation);
			contentBean.setMessaggio("Ti è stata inviata la procedura di cambio password alla email "+email);
			usernameResetPsw = "";
			emailPsw = "";

		} catch (Exception e1) {
			String msg = "username e/o email non corrette";
			contentBean.setMessaggio(msg);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public void passwordUpdate(String password) {
		Utente nuovo = utenteBean.getUtente();
		String salt;
		try {
			salt = HashingSHAE.getSalt();
			nuovo.setSalt(salt);
			String hashedPassword = HashingSHAE.get_SHA_512_SecurePassword(password, salt);
			nuovo.setPassword(hashedPassword);
			serv.merge(nuovo);
			contentBean.setMessaggio("Password aggiornata!");
			contentBean.setContent("homePage.xhtml");
			passwordReset = "";
			pswConfirmReset = "";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void checkConfirmPassword2(FacesContext context,
			UIComponent component, Object value) {

		boolean passwordok = false;	
		String confirm = (String) value;
		String valuepassword = (String)((UIInput)context.getViewRoot().findComponent("passwordresetform:password")).getValue();
        
		// pattern complessita password completo (1 num, 1 minu, 1 maiu, 1 car spec, no spazi, 5-10 car)
		//String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[,@#$%^&+=])(?=\\S+$).{5,10}";
		String pattern = "(?=.*[0-9])(?=.*[!?*.,@#$%^&+=])(?=\\S+$).{4,10}";
		passwordok = valuepassword.matches(pattern);
		if (!passwordok) {
			String msg = "la password deve avere minimo: 1 numero, 1 tra !?*.,@#$%^&+=, 4-10 caratteri, no spazi.";
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
		}
		
		if (valuepassword == null || !confirm.equals(valuepassword)) {
			String msg = "le password sono diverse";
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	
	
	
	
	
//	metodi NON usati (usati in registratiBean)
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
	
	
	
	



	public String getUsernameResetPsw() {
		return usernameResetPsw;
	}


	public String getEmailPsw() {
		return emailPsw;
	}


	public void setEmailPsw(String emailPsw) {
		this.emailPsw = emailPsw;
	}


	public void setUsernameResetPsw(String usernameResetPsw) {
		this.usernameResetPsw = usernameResetPsw;
	}


	public String getPswConfirmReset() {
		return pswConfirmReset;
	}

	public void setPswConfirmReset(String pswConfirmReset) {
		this.pswConfirmReset = pswConfirmReset;
	}



	public String getPasswordReset() {
		return passwordReset;
	}

	public void setPasswordReset(String passwordReset) {
		this.passwordReset = passwordReset;
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
	
	@ManagedProperty(value="#{annuncioBean}")
	private AnnuncioBean annuncioBean;
	
	@ManagedProperty(value="#{postBean}")
	private PostBean postBean;
	
	

		
	public AnnuncioBean getAnnuncioBean() {
		return annuncioBean;
	}


	public void setAnnuncioBean(AnnuncioBean annuncioBean) {
		this.annuncioBean = annuncioBean;
	}


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


	public PostBean getPostBean() {
		return postBean;
	}


	public void setPostBean(PostBean postBean) {
		this.postBean = postBean;
	}





}
