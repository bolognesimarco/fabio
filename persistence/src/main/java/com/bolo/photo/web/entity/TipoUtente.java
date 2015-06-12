package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class TipoUtente implements Serializable{
	private static final long serialVersionUID = -9019773926782820630L;

	@Id
	private int id;
	
	@Column
	private String descrizione;

	@ManyToMany(mappedBy="tipiUtente", cascade=CascadeType.ALL)
	private List<TipoLavoro> tipiLavoro = new ArrayList<TipoLavoro>();
	
	public List<TipoLavoro> getTipiLavoro() {
		return tipiLavoro;
	}

	public void setTipiLavoro(List<TipoLavoro> tipiLavoro) {
		this.tipiLavoro = tipiLavoro;
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
