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
public class Post implements Serializable{

	private static final long serialVersionUID = 7392892589549886583L;

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
    @JoinColumn(name="proponente", nullable=false)
	private Utente proponente;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="post")
	private List<Thread> risposte = new ArrayList<Thread>();

	@Column
	private String citt‡Post;
	
	@Column
	private boolean inVerifica;
	
	@Column
	private boolean postUtentiPlus;
	
	@Column
	private boolean postUtentiFree;
	
	@Column
	private boolean presentazioneUtentiFree;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="superpost", nullable=true, updatable=false)
	private SuperPost superpost;
	
//	GETTERS & SETTERS*****************
	
	
	
	
	public int getId() {
		return id;
	}

	public boolean isPostUtentiPlus() {
		return postUtentiPlus;
	}

	public void setPostUtentiPlus(boolean postUtentiPlus) {
		this.postUtentiPlus = postUtentiPlus;
	}

	public boolean isInVerifica() {
		return inVerifica;
	}

	public void setInVerifica(boolean inVerifica) {
		this.inVerifica = inVerifica;
	}

	public String getCitt‡Post() {
		return citt‡Post;
	}

	public void setCitt‡Post(String citt‡Post) {
		this.citt‡Post = citt‡Post;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Utente getProponente() {
		return proponente;
	}

	public void setProponente(Utente proponente) {
		this.proponente = proponente;
	}

	public List<Thread> getRisposte() {
		return risposte;
	}

	public void setRisposte(List<Thread> risposte) {
		this.risposte = risposte;
	}

	public boolean isPostUtentiFree() {
		return postUtentiFree;
	}

	public void setPostUtentiFree(boolean postUtentiFree) {
		this.postUtentiFree = postUtentiFree;
	}

	public boolean isPresentazioneUtentiFree() {
		return presentazioneUtentiFree;
	}

	public void setPresentazioneUtentiFree(boolean presentazioneUtentiFree) {
		this.presentazioneUtentiFree = presentazioneUtentiFree;
	}

	public SuperPost getSuperpost() {
		return superpost;
	}

	public void setSuperpost(SuperPost superpost) {
		this.superpost = superpost;
	}
	
	
	
}
