package com.bolo.photoshooters.vo;

import java.util.Date;
import java.util.List;

import com.bolo.photo.web.entity.RegioneItaliana;

public class CercaUtenteVO {
	
	private String name;
	private String username;
	private String password;
	private Date dataNascita;
	private int tipoUtente;
	private List <RegioneItaliana> regioniItaliane;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<RegioneItaliana> getRegioniItaliane() {
		return regioniItaliane;
	}
	public void setRegioniItaliane(List<RegioneItaliana> regioniItaliane) {
		this.regioniItaliane = regioniItaliane;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	public int getTipoUtente() {
		return tipoUtente;
	}
	public void setTipoUtente(int tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
	
}
