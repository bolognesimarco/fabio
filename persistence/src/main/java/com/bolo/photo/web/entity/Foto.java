package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Foto implements Serializable{

	private static final long serialVersionUID = -5640225111675845046L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column
	private String titolo;
	
	@Column
	private String nomeFileFoto;
	
	@Column
	private int altezzaFoto;
	
	@Column
	private int larghezzaFoto;
	
	@Column
	private boolean vietataMinori;
	
	@ManyToOne
    @JoinColumn(name="soggetto", nullable=true)
	private Utente soggetto;
	
	@ManyToOne
    @JoinColumn(name="fotografo", nullable=false)
	private Utente fotografo;
	
	public String getNomeFileFoto() {
		return nomeFileFoto;
	}

	public void setNomeFileFoto(String nomeFileFoto) {
		this.nomeFileFoto = nomeFileFoto;
	}

	@ManyToOne
    @JoinColumn(name="pubblicatore", nullable=true)
	private Utente pubblicatore;
	
	@ManyToOne
    @JoinColumn(name="album", nullable=false)
	private Album album;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFoto;
	
	public Album getAlbum() {
		return album;
	}

	public Date getDataFoto() {
		return dataFoto;
	}

	public void setDataFoto(Date dataFoto) {
		this.dataFoto = dataFoto;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	@ManyToMany
	@JoinTable(name="Foto_Visualizzatori")
	private List<Utente> visualizzatori;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="foto")
	private List<Voto> voti;
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAltezzaFoto() {
		return altezzaFoto;
	}

	public void setAltezzaFoto(int altezzaFoto) {
		this.altezzaFoto = altezzaFoto;
	}

	public int getLarghezzaFoto() {
		return larghezzaFoto;
	}

	public void setLarghezzaFoto(int larghezzaFoto) {
		this.larghezzaFoto = larghezzaFoto;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public boolean isVietataMinori() {
		return vietataMinori;
	}

	public void setVietataMinori(boolean vietataMinori) {
		this.vietataMinori = vietataMinori;
	}

	public Utente getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(Utente soggetto) {
		this.soggetto = soggetto;
	}

	public Utente getFotografo() {
		return fotografo;
	}

	public void setFotografo(Utente fotografo) {
		this.fotografo = fotografo;
	}

	public Utente getPubblicatore() {
		return pubblicatore;
	}

	public void setPubblicatore(Utente pubblicatore) {
		this.pubblicatore = pubblicatore;
	}

	public List<Utente> getVisualizzatori() {
		return visualizzatori;
	}

	public void setVisualizzatori(List<Utente> visualizzatori) {
		this.visualizzatori = visualizzatori;
	}

	public List<Voto> getVoti() {
		return voti;
	}

	public void setVoti(List<Voto> voti) {
		this.voti = voti;
	}

	
	
}
