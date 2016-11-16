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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class EmailDaInviare  implements Serializable{

	private static final long serialVersionUID = -3648316108980964360L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column
	private String email;
	
//	0=daInviare, 1=inInvio, 2=inviata
	@Column
	private int statoLavorazione = 0;

//	@Column
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date data;

	@JoinColumn(name="utenteSender", nullable=false)
	private Utente utenteSender;
	
	@Column
	private int tipoEmail;

	
	//*********GETTERS&SETTERS***********
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatoLavorazione() {
		return statoLavorazione;
	}

	public void setStatoLavorazione(int statoLavorazione) {
		this.statoLavorazione = statoLavorazione;
	}

//	public Date getData() {
//		return data;
//	}
//
//	public void setData(Date data) {
//		this.data = data;
//	}

	public Utente getUtenteSender() {
		return utenteSender;
	}

	public void setUtenteSender(Utente utenteSender) {
		this.utenteSender = utenteSender;
	}

	public int getTipoEmail() {
		return tipoEmail;
	}

	public void setTipoEmail(int tipoEmail) {
		this.tipoEmail = tipoEmail;
	}

	
}
