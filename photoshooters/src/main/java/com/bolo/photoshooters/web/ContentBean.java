package com.bolo.photoshooters.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ContentBean {
	private String content;
	private String contentPrecedente;
	
	private String messaggio;
	
	public String getContent() {
		System.out.println("gettingcontent:"+content);
		return content;
	}
	public void setContent(String content) {
		setContentPrecedente(this.content);
		this.content = content;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	public String getContentPrecedente() {
		return contentPrecedente;
	}
	public void setContentPrecedente(String contentPrecedente) {
		this.contentPrecedente = contentPrecedente;
	}
	public void indietro(){
		setContent(getContentPrecedente());
		setContentPrecedente("homePage.xhtml");
	}
}
