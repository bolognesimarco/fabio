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
public class Messaggio implements Serializable{
	private static final long serialVersionUID = -28507242949026175L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column 
	private String messaggio;
	
	@Column 
	private String oggetto;
	
	@ManyToMany 
	private List<Utente> letto = new ArrayList<Utente>();
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="thread", nullable=false, updatable=false)
	private Thread thread;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="mittente", nullable=false, updatable=false)
	private Utente mittente;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="destinatario", nullable=false, updatable=false)
	private Utente destinatario;
	
	
	@OneToOne(optional=true)
	@JoinColumn(name="precedente", nullable=true)
	private Messaggio precedente;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="precedente")
	private List<Messaggio> risposte;

	
	
//	**************GETTERS&SETTERS*************
	



	public List<Utente> getLetto() {
		return letto;
	}

	public void setLetto(List<Utente> letto) {
		this.letto = letto;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Utente getMittente() {
		return mittente;
	}

	public void setMittente(Utente mittente) {
		this.mittente = mittente;
	}

	public Utente getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Utente destinatario) {
		this.destinatario = destinatario;
	}

	public Messaggio getPrecedente() {
		return precedente;
	}

	public void setPrecedente(Messaggio precedente) {
		this.precedente = precedente;
	}

	public List<Messaggio> getRisposte() {
		return risposte;
	}

	public void setRisposte(List<Messaggio> risposte) {
		this.risposte = risposte;
	}

}
