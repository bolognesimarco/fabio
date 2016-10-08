package com.bolo.photoshooters.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.bolo.photo.web.entity.Foto;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;

@ManagedBean
@SessionScoped
public class MenuBean {
	private ServiziComuni serv = new ServiziComuniImpl();
	private String menuStyle1 = "";
	private String menuStyle2 = "";
	private String menuStyle3 = "";
	private String menuStyle4 = "";
	private String menuStyle5 = "";
	private String menuStyle6 = "";
	private String menuStyle7 = "";
	private String menuStyle8 = "";
	private String menuStyle9 = "";
	private String menuStyle10 = "";
	private String menuStyle11 = "";
	private String menuStyle12 = "";
	private String menuStyle13 = "";
	private String menuStyle14 = "";
	private String menuStyle15 = "";
	private String menuBorder = "text-shadow: 0.5px 0.5px 0.5px #fff;";
	private String menuUtenteBorder ="text-shadow: 0.5px 0.5px 0.5px #EF4323;";
	
	public void menuClick(int menuClicked){
		switch (menuClicked) {
		case 1://REGISTRATI
			contentBean.setContent("registrati.xhtml");
			azzeraMenuStyle();
			menuStyle1 = menuBorder;
			break;
		case 2://LOGIN
			contentBean.setContent("login.xhtml");
			azzeraMenuStyle();
			menuStyle2 = menuBorder;
			break;
		case 3://SITE
			contentBean.setMessaggio(null);
			contentBean.setContent("wip.xhtml");
			azzeraMenuStyle();
			menuStyle3 = menuBorder;
			break;
		case 4://FORUM
			contentBean.setMessaggio(null);
			contentBean.setContent("forum.xhtml");
			azzeraMenuStyle();
			menuStyle4 = menuBorder;
			break;
		case 5://LAVORI & EVENTI
			annuncioBean.cercaAnnunciSito();
			contentBean.setMessaggio(null);
			contentBean.setContent("lavoriEventi.xhtml");
			azzeraMenuStyle();
			menuStyle5 = menuBorder;
			break;
		case 6://PHOTOSHOOTERS
			parametersBean.fillEtà();
			contentBean.setMessaggio(null);
			contentBean.setContent("cerca3.xhtml");
			azzeraMenuStyle();
			menuStyle6 = menuBorder;
			break;
		case 7://HOME
			contentBean.setMessaggio(null);
			contentBean.setContent("homePage.xhtml");
			azzeraMenuStyle();
			menuStyle7 = menuBorder;
			break;
		case 8://PROFILO
			contentBean.setMessaggio(null);
			contentBean.setContent("profilo2.xhtml");
			azzeraMenuStyle();
			menuStyle8 = menuUtenteBorder;
			break;
		case 9://ALBUMS
			inputBean.setStatusMessage(null);
			contentBean.setMessaggio(null);
			contentBean.setContent("albums3.xhtml");
			azzeraMenuStyle();
			menuStyle9 = menuUtenteBorder;
			break;
		case 10://MESSAGGI
			contentBean.setMessaggio(null);
			contentBean.setContent("messaggi.xhtml");
			threadBean.getIdThreadsSelezionati().clear();
			azzeraMenuStyle();
			menuStyle10 = menuUtenteBorder;
			break;
		case 11://CHAT
			contentBean.setMessaggio(null);
			contentBean.setContent("wip.xhtml");
			azzeraMenuStyle();
			menuStyle11 = menuUtenteBorder;
			break;
		case 12://ANNUNCI
			contentBean.setMessaggio(null);
			contentBean.setContent("annunci.xhtml");
			annuncioBean.getIdAnnunciSelezionati().clear();
			annuncioBean.cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId()); 
			azzeraMenuStyle();
			menuStyle12 = menuUtenteBorder;
			break;
		case 13://CREDITI
			contentBean.setContent("wip.xhtml");
			azzeraMenuStyle();
			menuStyle13 = menuUtenteBorder;
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
			System.out.println("LOGOUTTTTT");
			azzeraMenuStyle();
			menuStyle14 = menuUtenteBorder;
			break;
		case 15://HOMEPAGE
			utenteBean.setUtente(null);
			contentBean.setContent("homePage.xhtml");
			break;
		case 16://MODIFICA PROFILO
			contentBean.setMessaggio(null);
			inputBean.setStatusMessage(null);
			contentBean.setContent("modificaProfilo4.xhtml");
			break;
		case 17://FOTOS
			contentBean.setMessaggio(null);
			contentBean.setContent("fotos.xhtml");
			break;
		case 18://NUOVO MESSAGGIO
			contentBean.setMessaggio(null);
			contentBean.setContent("nuovoMessaggio.xhtml");
			break;
		case 19://NUOVO MESSAGGIO CON DESTINATARIO
			contentBean.setMessaggio(null);
			threadBean.getMessaggio().setDestinatario(utenteBean.getCercaUtente().getUtente());
			contentBean.setContent("nuovoMessaggio.xhtml");
			break;
		case 20://NUOVO ANNUNCIO
			contentBean.setMessaggio(null);
			contentBean.setContent("nuovoAnnuncio.xhtml");
			break;
		case 21://RISPONDI AL POST
			contentBean.setMessaggio(null);
			contentBean.setContent("postRispondi.xhtml");
			break;
		default:
			break;
		}
	}

	public void azzeraMenuStyle () {
		menuStyle1 = "";
		menuStyle2 = "";
		menuStyle3 = "";
		menuStyle4 = "";
		menuStyle5 = "";
		menuStyle6 = "";
		menuStyle7 = "";
		menuStyle8 = "";
		menuStyle9 = "";
		menuStyle10 = "";
		menuStyle11 = "";
		menuStyle12 = "";
		menuStyle13 = "";
		menuStyle14 = "";
	}
	
	public void messaggioAvvenutaRegistrazione(){
		contentBean.setContent("messaggioAvvenutaRegistrazione.xhtml");
	}
	
	
	
//	****GETTERS&SETTERS**********************
	
	
	@ManagedProperty(value="#{contentBean}")
	private ContentBean contentBean;
	
	@ManagedProperty(value="#{utenteBean}")
	private UtenteBean utenteBean;

	@ManagedProperty(value="#{inputBean}")
	private InputBean inputBean;
	
	@ManagedProperty(value="#{parametersBean}")
	private ParametersBean parametersBean;
	
	@ManagedProperty(value="#{threadBean}")
	private ThreadBean threadBean;
			
	@ManagedProperty(value="#{annuncioBean}")
	private AnnuncioBean annuncioBean;
	
	@ManagedProperty(value="#{postBean}")
	private PostBean postBean;
	
	
	
	




	public String getMenuStyle8() {
		return menuStyle8;
	}

	public void setMenuStyle8(String menuStyle8) {
		this.menuStyle8 = menuStyle8;
	}

	public String getMenuStyle9() {
		return menuStyle9;
	}

	public void setMenuStyle9(String menuStyle9) {
		this.menuStyle9 = menuStyle9;
	}

	public String getMenuStyle10() {
		return menuStyle10;
	}

	public void setMenuStyle10(String menuStyle10) {
		this.menuStyle10 = menuStyle10;
	}

	public String getMenuStyle11() {
		return menuStyle11;
	}

	public void setMenuStyle11(String menuStyle11) {
		this.menuStyle11 = menuStyle11;
	}

	public String getMenuStyle12() {
		return menuStyle12;
	}

	public void setMenuStyle12(String menuStyle12) {
		this.menuStyle12 = menuStyle12;
	}

	public String getMenuStyle13() {
		return menuStyle13;
	}

	public void setMenuStyle13(String menuStyle13) {
		this.menuStyle13 = menuStyle13;
	}

	public String getMenuStyle14() {
		return menuStyle14;
	}

	public void setMenuStyle14(String menuStyle14) {
		this.menuStyle14 = menuStyle14;
	}

	public String getMenuStyle15() {
		return menuStyle15;
	}

	public void setMenuStyle15(String menuStyle15) {
		this.menuStyle15 = menuStyle15;
	}

	public String getMenuUtenteBorder() {
		return menuUtenteBorder;
	}

	public void setMenuUtenteBorder(String menuUtenteBorder) {
		this.menuUtenteBorder = menuUtenteBorder;
	}

	public String getMenuStyle3() {
		return menuStyle3;
	}

	public void setMenuStyle3(String menuStyle3) {
		this.menuStyle3 = menuStyle3;
	}

	public String getMenuStyle4() {
		return menuStyle4;
	}

	public void setMenuStyle4(String menuStyle4) {
		this.menuStyle4 = menuStyle4;
	}

	public String getMenuStyle5() {
		return menuStyle5;
	}

	public void setMenuStyle5(String menuStyle5) {
		this.menuStyle5 = menuStyle5;
	}

	public String getMenuStyle6() {
		return menuStyle6;
	}

	public void setMenuStyle6(String menuStyle6) {
		this.menuStyle6 = menuStyle6;
	}

	public String getMenuStyle7() {
		return menuStyle7;
	}

	public void setMenuStyle7(String menuStyle7) {
		this.menuStyle7 = menuStyle7;
	}

	public String getMenuStyle1() {
		return menuStyle1;
	}

	public void setMenuStyle1(String menuStyle1) {
		this.menuStyle1 = menuStyle1;
	}

	public String getMenuStyle2() {
		return menuStyle2;
	}

	public void setMenuStyle2(String menuStyle2) {
		this.menuStyle2 = menuStyle2;
	}

	public String getMenuBorder() {
		return menuBorder;
	}

	public void setMenuBorder(String menuBorder) {
		this.menuBorder = menuBorder;
	}

	public PostBean getPostBean() {
		return postBean;
	}

	public void setPostBean(PostBean postBean) {
		this.postBean = postBean;
	}

	public AnnuncioBean getAnnuncioBean() {
		return annuncioBean;
	}

	public void setAnnuncioBean(AnnuncioBean annuncioBean) {
		this.annuncioBean = annuncioBean;
	}

	public ThreadBean getThreadBean() {
		return threadBean;
	}

	public void setThreadBean(ThreadBean threadBean) {
		this.threadBean = threadBean;
	}

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

	public ContentBean getContentBean() {
		return contentBean;
	}

	public void setContentBean(ContentBean contentBean) {
		this.contentBean = contentBean;
	}
}
