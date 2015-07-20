package com.bolo.photoshooters.web;


import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.service.ServiziVari;
import com.bolo.photoshooters.service.ServiziVariImpl;

@ManagedBean
@SessionScoped
public class RegistratiBean {
	private String username;
	private String password;
	private String confirm;
	private String email;
	private String nome;
	private int tipoUtente;
	
	private ServiziComuni serv = new ServiziComuniImpl();

	public void registrati() {
		
		try {
			Utente nuovo = new Utente();
			nuovo.setUsername(username);
			nuovo.setPassword(password);
			nuovo.setName(nome);
			nuovo.setEmail(email);
			nuovo.setTipoUtente(serv.getReference(TipoUtente.class, tipoUtente));
			nuovo.setActive(false);
			nuovo.setActivationCode(UUID.randomUUID().toString());
			serv.persist(nuovo);
			MailSender.sendRegisterMail(email, nuovo.getActivationCode());
		} catch (Exception e) {
			e.printStackTrace();
			contentBean.setContent("registrati.xhtml");
		}

		contentBean.setContent("messaggioInvioMailRegistrazione.xhtml");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
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
			String msg = "username gi� in uso";
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
		}
		
	}

	
	public void checkEmail(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String emailToCheck = (String) value;
		
		boolean esiste2 = false;
		boolean mailok = false;
		boolean attivo = false;
		 
        // Create the Pattern using the regex
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
 
        // Match the given string with the pattern
        Matcher m = p.matcher(emailToCheck);
 
        // check whether match is found
        boolean matchFound = m.matches();
 
        StringTokenizer st = new StringTokenizer(emailToCheck, ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }
 
        // validate the country code
        if (matchFound && lastToken.length() >= 2
                && emailToCheck.length() - 1 != lastToken.length()) {
 
            mailok=false;
        } else {
            mailok=true;
        }
		
		try {
			esiste2 = serviziVari.emailEsiste(emailToCheck);
			attivo = serviziVari.utenteAttivo(emailToCheck);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "GRAVE", "GRAVE"));
		}
		if (esiste2) {
			if (attivo){
				String msg = "email gi� utilizzata ma non registrata";
				throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
			}	
			else
			{
				String msg = "email gi� in uso";
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
			}
		}
	
		if (mailok) {
			String msg = "email non valida";
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, msg, msg));
		}
		
	}
	
	public void checkConfirmPassword(FacesContext context,
			UIComponent component, Object value) {

		boolean passwordok = false;	
		String confirm = (String) value;
		String valuepassword = (String)((UIInput)context.getViewRoot().findComponent("registratiform:password")).getValue();
        
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(int tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
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
