package com.bolo.photoshooters.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bolo.photo.web.entity.Esperienza;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoLavoro;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;

public class CercaUtenteVO {
	
	private String name;
	private String username;
	private String password;
	private Date dataNascita;
	private int tipoUtente;
	private List <RegioneItaliana> regioniitaliane = new ArrayList<RegioneItaliana>();
	private Esperienza esperienza;
	private List<TipoLavoro> tipiLavoro = new ArrayList<TipoLavoro>();
	private Sesso sesso;
	private Utente utente;
	private boolean online;
	private int lastOnlineIscritto = 1; 
	
	public Sesso getSesso() {
		return sesso;
	}
	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}
	public String getName() {
		return name;
	}
	public int getLastOnlineIscritto() {
		return lastOnlineIscritto;
	}
	public void setLastOnlineIscritto(int lastOnlineIscritto) {
		this.lastOnlineIscritto = lastOnlineIscritto;
	}
	public List<TipoLavoro> getTipiLavoro() {
		return tipiLavoro;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public void setTipiLavoro(List<TipoLavoro> tipiLavoro) {
		this.tipiLavoro = tipiLavoro;
	}
	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	public List<String> getTipiLavoroDescrizione() {
		List<String> darit = new ArrayList<String>();
		for (TipoLavoro tipo : getTipiLavoro()) {
			darit.add(tipo.getDescrizione());
		}
		return darit;
	}
	public int getTipoUtente() {
		return tipoUtente;
	}
	public void setTipoUtente(int tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
	public Esperienza getEsperienza() {
		return esperienza;
	}
	public void setEsperienza(Esperienza esperienza) {
		this.esperienza = esperienza;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<RegioneItaliana> getRegioniitaliane() {
		return regioniitaliane;
	}
	public void setRegioniitaliane(List<RegioneItaliana> regioniitaliane) {
		this.regioniitaliane = regioniitaliane;
	}
	public List<String> getRegIta(){
		List<String> darit = new ArrayList<String>();
		for (RegioneItaliana reg : getRegioniitaliane()) {
			darit.add(reg.getRegioneitaliana());
		}
		return darit;
	}
	public void setRegIta(List<String> regs){
		getRegioniitaliane().clear();
		for (String string : regs) {
				getRegioniitaliane().add(RegioneItaliana.valueOf(string));
		}
	}
	public void setTipiLavoroDescrizione(List<String> lavs){
		getTipiLavoro().clear();
		for (String string : lavs) {
				getTipiLavoroDescrizione().add(string);
		}
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
	
}
