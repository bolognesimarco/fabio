package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Annuncio implements Serializable{

	private static final long serialVersionUID = 3862344985425719117L;

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
    @JoinColumn(name="proponente", nullable=false)
	private Utente proponente;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="annuncio")
	private List<Thread> risposte = new ArrayList<Thread>();

	@Column
	private String citt�Annuncio;
	
	@ElementCollection
	private List <RegioneItaliana> regioniAnnuncio = new ArrayList<RegioneItaliana>();
	
	
	
//	GETTERS & SETTERS*****************
	
	
	
	public int getId() {
		return id;
	}

	public List<RegioneItaliana> getRegioniAnnuncio() {
		return regioniAnnuncio;
	}

	public void setRegioniAnnuncio(List<RegioneItaliana> regioniAnnuncio) {
		this.regioniAnnuncio = regioniAnnuncio;
	}

	public String getCitt�Annuncio() {
		return citt�Annuncio;
	}

	public void setCitt�Annuncio(String citt�Annuncio) {
		this.citt�Annuncio = citt�Annuncio;
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
	
	public void setRegIta(List<String> regs){
		getRegioniAnnuncio().clear();
		for (String string : regs) {
			getRegioniAnnuncio().add(RegioneItaliana.valueOf(string));
		}
	}
	
	public List<String> getRegIta(){
		List<String> darit = new ArrayList<String>();
		for (RegioneItaliana reg : getRegioniAnnuncio()) {
			darit.add(reg.getRegioneitaliana());
		}
		return darit;
	}
	
}
