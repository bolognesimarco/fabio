package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Utente implements Serializable{
	private static final long serialVersionUID = -8221785427181227323L;

	@Id
	@GeneratedValue
	private int id;

	@Column
	private String name;
	
	@Column
	private String username;
	
	@Column
	private String email;
	
	@Column
	private String activationCode;
	
	@Column
	private boolean active = false;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataIscrizione;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataMember;
	
	@Column
	private String password;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascita;
	
	@ManyToOne
    @JoinColumn(name="tipoUtente", nullable=false)
	private TipoUtente tipoUtente;
	
	@OneToMany
	@JoinColumn(name="UtenteId")
	private List<Utente> collaboratori;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="utente")
	private List<Membership> memberships;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="rilasciatoDa")
	private List<Feedback> feedbackRilasciati;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="ricevutoDa")
	private List<Feedback> feedbackRicevuti;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="soggetto")
	private List<Foto> soggettoDi;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="fotografo")
	private List<Foto> fotografoDi;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="pubblicatore")
	private List<Foto> pubblicate;
	
	@ManyToMany(mappedBy="visualizzatori")
	private List<Foto> visualizzate;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="rilasciatoDa")
	private List<Voto> voti;
	
	@Enumerated
	private Sesso sesso;

	@Enumerated
	private Esperienza esperienza;

	@Column
	private String sitoweb;	

	@Enumerated
	private RegioniItaliane regioniitaliane;

	public RegioniItaliane getRegioniitaliane() {
		return regioniitaliane;
	}

	public void setRegioniitaliane(RegioniItaliane regioniitaliane) {
		this.regioniitaliane = regioniitaliane;
	}

	public Esperienza[] getEsperienze() {
        return Esperienza.values();
	}
	
	public Esperienza getEsperienza() {
		return esperienza;
	}

	public void setEsperienza(Esperienza esperienza) {
		this.esperienza = esperienza;
	}

	public String getSitoweb() {
		return sitoweb;
	}

	public void setSitoweb(String sitoweb) {
		this.sitoweb = sitoweb;
	}

	public Sesso[] getSessi() {
        return Sesso.values();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDataIscrizione(Date dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public Date getDataMember() {
		return dataMember;
	}

	public void setDataMember(Date dataMember) {
		this.dataMember = dataMember;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	public List<Utente> getCollaboratori() {
		return collaboratori;
	}

	public void setCollaboratori(List<Utente> collaboratori) {
		this.collaboratori = collaboratori;
	}

	public List<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	public List<Feedback> getFeedbackRilasciati() {
		return feedbackRilasciati;
	}

	public void setFeedbackRilasciati(List<Feedback> feedbackRilasciati) {
		this.feedbackRilasciati = feedbackRilasciati;
	}

	public List<Feedback> getFeedbackRicevuti() {
		return feedbackRicevuti;
	}

	public void setFeedbackRicevuti(List<Feedback> feedbackRicevuti) {
		this.feedbackRicevuti = feedbackRicevuti;
	}

	public List<Foto> getSoggettoDi() {
		return soggettoDi;
	}

	public void setSoggettoDi(List<Foto> soggettoDi) {
		this.soggettoDi = soggettoDi;
	}

	public List<Foto> getFotografoDi() {
		return fotografoDi;
	}

	public void setFotografoDi(List<Foto> fotografoDi) {
		this.fotografoDi = fotografoDi;
	}

	public List<Foto> getPubblicate() {
		return pubblicate;
	}

	public void setPubblicate(List<Foto> pubblicate) {
		this.pubblicate = pubblicate;
	}

	public List<Foto> getVisualizzate() {
		return visualizzate;
	}

	public void setVisualizzate(List<Foto> visualizzate) {
		this.visualizzate = visualizzate;
	}

	public List<Voto> getVoti() {
		return voti;
	}

	public void setVoti(List<Voto> voti) {
		this.voti = voti;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public Sesso getSesso() {
		return sesso;
	}

	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
