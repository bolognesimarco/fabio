package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class TipoLavoro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3363683251993722311L;

	@Id
	private int id;

	@Column
	private String descrizione;

	@ManyToMany
	private List<TipoUtente> tipiUtente = new ArrayList<TipoUtente>();

	@ManyToMany (mappedBy="tipiLavoro")
	private List<Utente> utenti;
	
	
	public List<Utente> getUtenti() {
		return utenti;
	}

	public void setUtenti(List<Utente> utenti) {
		this.utenti = utenti;
	}

	public List<TipoUtente> getTipiUtente() {
		return tipiUtente;
	}

	public void setTipiUtente(List<TipoUtente> tipiUtente) {
		this.tipiUtente = tipiUtente;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	
}
