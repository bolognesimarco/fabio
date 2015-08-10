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
public class Album  implements Serializable{
	
	private static final long serialVersionUID = 4475766647760160199L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column
	private String titolo;
	

	@ManyToOne
    @JoinColumn(name="pubblicatore", nullable=false)
	private Utente pubblicatore;
	
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="album")
	private List<Foto> fotos = new ArrayList<Foto>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public Utente getPubblicatore() {
		return pubblicatore;
	}

	public void setPubblicatore(Utente pubblicatore) {
		this.pubblicatore = pubblicatore;
	}

	public List<Foto> getFotos() {
		return fotos;
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}
	
	
}
