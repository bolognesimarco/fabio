package com.bolo.photoshooters.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class MenuBean {
	
	public void menuClick(int menuClicked){
		switch (menuClicked) {
		case 1://REGISTRATI
			contentBean.setContent("registrati.xhtml");
			break;
		case 2://LOGIN
			contentBean.setContent("login.xhtml");
			break;
		case 3://SITE
			contentBean.setContent("wip.xhtml");
			break;
		case 4://FORUM
			contentBean.setContent("uploadPhoto.xhtml");
			break;
		case 5://LAVORI
			contentBean.setContent("wip.xhtml");
			break;
		case 6://PHOTOSHOOTERS
			contentBean.setContent("cerca.xhtml");
			break;
		case 7://HOME
			contentBean.setMessaggio(null);
			contentBean.setContent("homePage.xhtml");
			break;
		case 8://PROFILO
			contentBean.setMessaggio(null);
			contentBean.setContent("profilo.xhtml");
			//utenteBean.fillSelectItems();
			break;
		case 9://FORUM
			contentBean.setContent("wip.xhtml");
			break;
		case 10://FORUM
			contentBean.setContent("wip.xhtml");
			break;
		case 11://FORUM
			contentBean.setContent("wip.xhtml");
			break;
		case 12://FORUM
			contentBean.setContent("wip.xhtml");
			break;
		case 13://FORUM
			contentBean.setContent("wip.xhtml");
			break;
		case 14://LOGOUT
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
			contentBean.setContent("modificaProfilo.xhtml");
			break;
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

	public UtenteBean getUtenteBean() {
		return utenteBean;
	}

	public void setUtenteBean(UtenteBean utenteBean) {
		this.utenteBean = utenteBean;
	}
	
}
