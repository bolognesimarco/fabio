package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Membership implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1432701753092277774L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInizio;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFine;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="tipoMembership", nullable=false, updatable=false)
	private TipoMembership tipoMembership;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="utente", nullable=false, updatable=false)
	private Utente utente;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public TipoMembership getTipoMembership() {
		return tipoMembership;
	}

	public void setTipoMembership(TipoMembership tipoMembership) {
		this.tipoMembership = tipoMembership;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
}
