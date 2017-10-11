package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ChatMessaggio implements Serializable{
	private static final long serialVersionUID = 5650461599333339474L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column 
	private String messaggioChat;
	
//	@Column 
//	private String oggetto;
	
//	@ManyToMany 
//	private List<Utente> letto = new ArrayList<Utente>();
	
	@Column
	private boolean lettoDaDestinatario = false;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataChat;
	
	@Column
	private String mittenteChat;
	
	@Column
	private String destinatarioChat;
	
//	@ManyToOne(optional=false)
//	@JoinColumn(name="thread", nullable=false, updatable=false)
//	private Thread thread;
	
//	@ManyToOne(optional=false)
//	@JoinColumn(name="mittente", nullable=false, updatable=false)
//	private Utente mittente;
//	
//	@ManyToOne(optional=false)
//	@JoinColumn(name="destinatario", nullable=false, updatable=false)
//	private Utente destinatario;
	
	
//	@OneToOne(optional=true)
//	@JoinColumn(name="precedente", nullable=true)
//	private Messaggio precedente;
//	
//	@OneToMany(cascade=CascadeType.ALL, mappedBy="precedente")
//	private List<Messaggio> risposte;

	
	
//	**************GETTERS&SETTERS*************
	



//	public List<Utente> getLetto() {
//		return letto;
//	}
//
//	public void setLetto(List<Utente> letto) {
//		this.letto = letto;
//	}

	
	
	public int getId() {
		return id;
	}
	
	public String getMittenteChat() {
		return mittenteChat;
	}

	public void setMittenteChat(String mittenteChat) {
		this.mittenteChat = mittenteChat;
	}

	public String getDestinatarioChat() {
		return destinatarioChat;
	}

	public void setDestinatarioChat(String destinatarioChat) {
		this.destinatarioChat = destinatarioChat;
	}

	public void setId(int id) {
		this.id = id;
	}


//	public String getOggetto() {
//		return oggetto;
//	}
//
//	public void setOggetto(String oggetto) {
//		this.oggetto = oggetto;
//	}


//	public Thread getThread() {
//		return thread;
//	}
//
//	public void setThread(Thread thread) {
//		this.thread = thread;
//	}

//	public Utente getMittente() {
//		return mittente;
//	}
//
//	public void setMittente(Utente mittente) {
//		this.mittente = mittente;
//	}
//
//	public Utente getDestinatario() {
//		return destinatario;
//	}
//
//	public void setDestinatario(Utente destinatario) {
//		this.destinatario = destinatario;
//	}

	public String getMessaggioChat() {
		return messaggioChat;
	}

	public void setMessaggioChat(String messaggioChat) {
		this.messaggioChat = messaggioChat;
	}

	public Date getDataChat() {
		return dataChat;
	}

	public void setDataChat(Date dataChat) {
		this.dataChat = dataChat;
	}

	public boolean isLettoDaDestinatario() {
		return lettoDaDestinatario;
	}

	public void setLettoDaDestinatario(boolean lettoDaDestinatario) {
		this.lettoDaDestinatario = lettoDaDestinatario;
	}

	
	
//	public Messaggio getPrecedente() {
//		return precedente;
//	}
//
//	public void setPrecedente(Messaggio precedente) {
//		this.precedente = precedente;
//	}
//
//	public List<Messaggio> getRisposte() {
//		return risposte;
//	}
//
//	public void setRisposte(List<Messaggio> risposte) {
//		this.risposte = risposte;
//	}

}
