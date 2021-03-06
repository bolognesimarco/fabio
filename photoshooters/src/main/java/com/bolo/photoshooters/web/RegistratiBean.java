package com.bolo.photoshooters.web;


import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.bolo.photo.web.entity.EmailDaInviare;
import com.bolo.photo.web.entity.Foto;
import com.bolo.photo.web.entity.Membership;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoMembership;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.service.ServiziVari;
import com.bolo.photoshooters.service.ServiziVariImpl;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.bolo.photoshooters.util.HashingSHAE;

@ManagedBean
@SessionScoped
public class RegistratiBean {
	private String username;
	private String password;
	private String confirm;
	private String email;
	private String nome;
	private int tipoUtente;
	private Sesso sesso; 
	private String citt�;
	private boolean accettoRegolamento = false;
	private int tipoMembership = 1;
	
	private ServiziComuni serv = new ServiziComuniImpl();
	private ServiziVari serviziVari = new ServiziVariImpl();
	
	public void registrati() {
		
		if (accettoRegolamento){
			try {
				Utente nuovo = new Utente();
				nuovo.setUsername(username);
//				hashing password SHAE512
				String salt = HashingSHAE.getSalt();
				nuovo.setSalt(salt);
				String hashedPassword = HashingSHAE.get_SHA_512_SecurePassword(password, salt);
				nuovo.setPassword(hashedPassword);
				nuovo.setName(nome);
				nuovo.setEmail(email);
				nuovo.setSesso(sesso);
				nuovo.setCitt�(citt�);
				nuovo.setTipoUtente(serv.getReference(TipoUtente.class, tipoUtente));
				nuovo.setActive(false);
				nuovo.setActivationCode(UUID.randomUUID().toString());
				Date currentDate = new Date();
				nuovo.setDataIscrizione(currentDate);
				
//				membership selection
				DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Membership newMShip = new Membership();
				EntityManager em = EMF.createEntityManager();
				String hql = "from TipoMembership tm";
				Query q = em.createQuery(hql, TipoMembership.class);
				List<TipoMembership> tipimerbership = q.getResultList();
				System.out.println("tipimerbership size "+tipimerbership.size());
				if(tipoMembership==1) { //FREe
					
					newMShip.setTipoMembership(tipimerbership.get(1));
//					Date dataInizio = new Date();
//					String strdataInizio = "02-04-2013 00:35:42";
//					dataInizio = dateformat.parse(strdataInizio);
					newMShip.setDataInizio(currentDate);
//					Date dataFine = new Date();passwordReset.xhtml
//					String strdataFine = "02-04-2017 00:35:42";
//					dataFine = dateformat.parse(strdataFine);
//					newMShip.setDataFine(dataFine);
					newMShip.setUtente(nuovo);
					List<Membership> listaMShip = new ArrayList<Membership>();
					listaMShip.add(newMShip);
					nuovo.setMemberships(listaMShip);
				} else { //PLUS
					newMShip.setTipoMembership(tipimerbership.get(2));
					contentBean.setContent("pagamento.xhtml");
				}

				serv.persist(nuovo);
				EmailDaInviare emailActivation = new EmailDaInviare();
				emailActivation.setTipoEmail(9);
				emailActivation.setUtenteSender(nuovo);
				emailActivation.setEmail(email);
				serv.persist(emailActivation);
//				MailSender.sendRegisterMail(emailActivation, nuovo.getActivationCode());
			} catch (Exception e) {
				e.printStackTrace();
				contentBean.setContent("registrati.xhtml");
			}
	
			contentBean.setContent("messaggioInvioMailRegistrazione.xhtml");
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
			}
		else {
			contentBean.setMessaggio("per completare la registrazione � obbligatorio accettare il regolamento!");
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
	
	public List<String> suggerisciCitt� (String citta) throws Exception{
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAg7ZoORXv7d2eMGKb-pB7_QZReIPiIzxw");

		GeocodingResult[] results =  GeocodingApi.geocode(context, citta).await();
		List<String> suggerimenti = new ArrayList<String>();
		
		for (GeocodingResult geocodingResult : results) {
//			System.out.println(geocodingResult.formattedAddress);	
			suggerimenti.add(geocodingResult.formattedAddress);
			setCitt�(geocodingResult.formattedAddress);
		}
		return suggerimenti;
		
		}
	
	

//	 getters & setters ************************************************************************
	
	
	
	
	public boolean isAccettoRegolamento() {
		return accettoRegolamento;
	}

	public int getTipoMembership() {
		return tipoMembership;
	}


	public void setTipoMembership(int tipoMembership) {
		this.tipoMembership = tipoMembership;
	}


	public void setAccettoRegolamento(boolean accettoRegolamento) {
		this.accettoRegolamento = accettoRegolamento;
	}

	public Sesso getSesso() {
		return sesso;
	}


	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
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

	public String getCitt�() {
		return citt�;
	}


	public void setCitt�(String citt�) {
		this.citt� = citt�;
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
