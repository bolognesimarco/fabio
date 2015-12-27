package com.bolo.photo.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Voto {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column
	private int score;
	
	@Column
	private String messaggio;
	
	@ManyToOne
    @JoinColumn(name="rilasciatoDa", nullable=false)
	private Utente rilasciatoDa;
	
	@ManyToOne
    @JoinColumn(name="foto", nullable=false)
	private Foto foto;

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public Utente getRilasciatoDa() {
		return rilasciatoDa;
	}

	public void setRilasciatoDa(Utente rilasciatoDa) {
		this.rilasciatoDa = rilasciatoDa;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

}
