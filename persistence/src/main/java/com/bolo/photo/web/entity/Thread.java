package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Thread implements Serializable{
	
	private static final long serialVersionUID = 1378092449665363764L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column
	private boolean cancellatoMittentePrimo;
	
	@Column
	private boolean cancellatoDestinatarioPrimo;
	
	@Column
	private boolean nuovoMessaggio = false;
	
	@Column
	private boolean cancellatoThreadMittente = false;
	
	@Column
	private boolean cancellatoThreadDestinatario = false;
	
	@Column
	private String oggettoThread;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="annuncio", nullable=true, updatable=false)
	private Annuncio annuncio;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="thread")
	private List<Messaggio> messaggi = new ArrayList<Messaggio>();
	
	@ManyToOne(optional=false)
	@JoinColumn(name="mittentePrimo", nullable=false, updatable=false)
	private Utente mittentePrimo;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="destinatarioPrimo", nullable=false, updatable=false)
	private Utente destinatarioPrimo;

	
	
//	**********GETTERS&SETTERS**************
	
	

	public boolean isCancellatoThreadMittente() {
		return cancellatoThreadMittente;
	}

	public void setCancellatoThreadMittente(boolean cancellatoThreadMittente) {
		this.cancellatoThreadMittente = cancellatoThreadMittente;
	}

	public boolean isCancellatoThreadDestinatario() {
		return cancellatoThreadDestinatario;
	}

	public void setCancellatoThreadDestinatario(boolean cancellatoThreadDestinatario) {
		this.cancellatoThreadDestinatario = cancellatoThreadDestinatario;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getOggettoThread() {
		return oggettoThread;
	}

	public void setOggettoThread(String oggettoThread) {
		this.oggettoThread = oggettoThread;
	}

	public boolean isCancellatoMittentePrimo() {
		return cancellatoMittentePrimo;
	}

	public void setCancellatoMittentePrimo(boolean cancellatoMittentePrimo) {
		this.cancellatoMittentePrimo = cancellatoMittentePrimo;
	}

	public boolean isCancellatoDestinatarioPrimo() {
		return cancellatoDestinatarioPrimo;
	}

	public void setCancellatoDestinatarioPrimo(boolean cancellatoDestinatarioPrimo) {
		this.cancellatoDestinatarioPrimo = cancellatoDestinatarioPrimo;
	}
	
	public boolean isNuovoMessaggio() {
		return nuovoMessaggio;
	}

	public void setNuovoMessaggio(boolean nuovoMessaggio) {
		this.nuovoMessaggio = nuovoMessaggio;
	}

	public Annuncio getAnnuncio() {
		return annuncio;
	}

	public void setAnnuncio(Annuncio annuncio) {
		this.annuncio = annuncio;
	}

	public List<Messaggio> getMessaggi() {
		return messaggi;
	}

	public void setMessaggi(List<Messaggio> messaggi) {
		this.messaggi = messaggi;
	}

	public Utente getMittentePrimo() {
		return mittentePrimo;
	}

	public void setMittentePrimo(Utente mittentePrimo) {
		this.mittentePrimo = mittentePrimo;
	}

	public Utente getDestinatarioPrimo() {
		return destinatarioPrimo;
	}

	public void setDestinatarioPrimo(Utente destinatarioPrimo) {
		this.destinatarioPrimo = destinatarioPrimo;
	}
}
