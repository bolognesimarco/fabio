package com.bolo.photoshooters.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.temp.InputBean;

@ManagedBean
@SessionScoped
public class MenuBean {
	private ServiziComuni serv = new ServiziComuniImpl();
	
	public void menuClick(int menuClicked){
		switch (menuClicked) {
		case 1://REGISTRATI
			contentBean.setContent("registrati.xhtml");
			break;
		case 2://LOGIN
			contentBean.setContent("login.xhtml");
			break;
		case 3://SITE
			contentBean.setMessaggio(null);
			contentBean.setContent("wip.xhtml");
			break;
		case 4://FORUM
			contentBean.setMessaggio(null);
			contentBean.setContent("localizzazione.xhtml");
			break;
		case 5://LAVORI
			contentBean.setMessaggio(null);
			contentBean.setContent("foto.xhtml");
			break;
		case 6://PHOTOSHOOTERS
			parametersBean.fillEt�();
			contentBean.setMessaggio(null);
			contentBean.setContent("cerca3.xhtml");
			break;
		case 7://HOME
			contentBean.setMessaggio(null);
			contentBean.setContent("homePage.xhtml");
			break;
		case 8://PROFILO
			contentBean.setMessaggio(null);
			contentBean.setContent("profilo2.xhtml");
			break;
		case 9://ALBUMS
			//utenteBean.albumsUtente();
			inputBean.setStatusMessage(null);
			contentBean.setMessaggio(null);
			contentBean.setContent("albums2.xhtml");
			break;
		case 10://MESSAGGI
			contentBean.setMessaggio(null);
			contentBean.setContent("wip.xhtml");
			break;
		case 11://CHAT
			contentBean.setContent("wip.xhtml");
			break;
		case 12://ANNUNCI
			contentBean.setContent("wip.xhtml");
			break;
		case 13://CREDITI
			contentBean.setContent("wip.xhtml");
			break;
		case 14://LOGOUT
			utenteBean.getUtente().setOnline(false);
			try {
				serv.merge(utenteBean.getUtente());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			utenteBean.setUtente(null);
			contentBean.setMessaggio(null);
			contentBean.setContent("homePage.xhtml");
			break;
		case 15://HOMEPAGE
			utenteBean.setUtente(null);
			contentBean.setContent("homePage.xhtml");
			break;
		case 16://MODIFICA PROFILO
			contentBean.setMessaggio(null);
			inputBean.setStatusMessage(null);
			contentBean.setContent("modificaProfilo2.xhtml");
			break;
//		case 17://NUOVO ALBUM_non pi� usato!
//			contentBean.setMessaggio(null);
//			contentBean.setContent("nuovoAlbum.xhtml");
//			break;
		default:
			break;
		}
	}


	
	public void messaggioAvvenutaRegistrazione(){
		contentBean.setContent("messaggioAvvenutaRegistrazione.xhtml");
	}
	
	public ContentBean getContentBean() {
		return contentBean;
	}

	public void setContentBean(ContentBean contentBean) {
		this.contentBean = contentBean;
	}
	
	@ManagedProperty(value="#{contentBean}")
	private ContentBean contentBean;
	
	@ManagedProperty(value="#{utenteBean}")
	private UtenteBean utenteBean;

	@ManagedProperty(value="#{inputBean}")
	private InputBean inputBean;
	
	@ManagedProperty(value="#{parametersBean}")
	private ParametersBean parametersBean;
	
	public UtenteBean getUtenteBean() {
		return utenteBean;
	}

	public ParametersBean getParametersBean() {
		return parametersBean;
	}



	public void setParametersBean(ParametersBean parametersBean) {
		this.parametersBean = parametersBean;
	}



	public InputBean getInputBean() {
		return inputBean;
	}

	public void setInputBean(InputBean inputBean) {
		this.inputBean = inputBean;
	}

	public void setUtenteBean(UtenteBean utenteBean) {
		this.utenteBean = utenteBean;
	}
	
}
