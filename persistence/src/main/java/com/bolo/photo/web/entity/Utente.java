package com.bolo.photo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
	private String cognome;
	
	@Column
	private String username;
	
	@Column
	private String email;
	
	@Column
	private String activationCode;
	
	@Column
	private boolean active = false;

	@Column
	private String avatar;
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

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
	
	@ManyToMany
	private List<TipoLavoro> tipiLavoro = new ArrayList<TipoLavoro>();
	
	public List<TipoLavoro> getTipiLavoro() {
		return tipiLavoro;
	}
	
	public List<String> getTipiLavoroDescrizione() {
		List<String> darit = new ArrayList<String>();
		for (TipoLavoro tipo : getTipiLavoro()) {
			darit.add(tipo.getDescrizione());
		}
		return darit;
	}

	public void setTipiLavoro(List<TipoLavoro> tipiLavoro) {
		
		this.tipiLavoro = tipiLavoro;
	}
	
	@ManyToOne
    @JoinColumn(name="tipoUtente", nullable=false)
	private TipoUtente tipoUtente;
	
	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
	
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
	private List<Album> pubblicati;
	
	@ManyToMany(mappedBy="visualizzatori")
	private List<Foto> visualizzate;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="rilasciatoDa")
	private List<Voto> voti;
	
	@Enumerated
	private Sesso sesso;

	@Column
	private String descrizione;
	
	@Column
	private String città;
	
	@Enumerated
	private Esperienza esperienza;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCittà() {
		return città;
	}

	public void setCittà(String città) {
		this.città = città;
	}

	@Column
	private String sitoweb;	
	
	@Column
	private String facebook;
	
	@Column
	private String twitter;
	
	@Column
	private String tumblr;
	
	@Column
	private String instagram;
	
	@Column
	private String linkedin;
	
	@Column
	private String googleplus;
	
	@Column
	private Integer altezza;
	
	@Column
	private Integer peso;
	
	@Column
	private Integer taglia;
	
	@Column
	private Integer seno;

	@Column
	private Integer vita;
	
	@Column
	private Integer fianchi;
	
	@Column
	private Integer scarpe;
	
	@Column
	private Integer tatuaggi;

	@Column
	private Integer piercing;
	
	@ElementCollection
	private List <RegioneItaliana> regioniitaliane;
	
	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getTumblr() {
		return tumblr;
	}

	public void setTumblr(String tumblr) {
		this.tumblr = tumblr;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public String getGoogleplus() {
		return googleplus;
	}

	public void setGoogleplus(String googleplus) {
		this.googleplus = googleplus;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public List<RegioneItaliana> getRegioniitaliane() {
		return regioniitaliane;
	}

	public void setRegioniitaliane(List<RegioneItaliana> regioniitaliane) {
		this.regioniitaliane = regioniitaliane;
	}

	public void setRegIta(List<String> regs){
		getRegioniitaliane().clear();
		for (String string : regs) {
				getRegioniitaliane().add(RegioneItaliana.valueOf(string));
		}
	}
	public List<String> getRegIta(){
		List<String> darit = new ArrayList<String>();
		for (RegioneItaliana reg : getRegioniitaliane()) {
			darit.add(reg.getRegioneitaliana());
		}
		return darit;
	}
	
	public Esperienza getEsperienza() {
		return esperienza;
	}

	public void setEsperienza(Esperienza esperienza) {
		this.esperienza = esperienza;
	}

	public Integer getTatuaggi() {
		return tatuaggi;
	}

	public void setTatuaggi(Integer tatuaggi) {
		this.tatuaggi = tatuaggi;
	}

	public Integer getPiercing() {
		return piercing;
	}

	public void setPiercing(Integer piercing) {
		this.piercing = piercing;
	}
	
	public Integer getAltezza() {
		return altezza;
	}

	public void setAltezza(Integer altezza) {
		this.altezza = altezza;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public Integer getTaglia() {
		return taglia;
	}

	public void setTaglia(Integer taglia) {
		this.taglia = taglia;
	}

	public Integer getSeno() {
		return seno;
	}

	public void setSeno(Integer seno) {
		this.seno = seno;
	}

	public Integer getVita() {
		return vita;
	}

	public void setVita(Integer vita) {
		this.vita = vita;
	}

	public Integer getFianchi() {
		return fianchi;
	}

	public void setFianchi(Integer fianchi) {
		this.fianchi = fianchi;
	}

	public Integer getScarpe() {
		return scarpe;
	}

	public void setScarpe(Integer scarpe) {
		this.scarpe = scarpe;
	}
	
	public String getSitoweb() {
		return sitoweb;
	}

	public void setSitoweb(String sitoweb) {
		this.sitoweb = sitoweb;
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

	public List<Album> getPubblicati() {
		return pubblicati;
	}

	public void setPubblicati(List<Album> pubblicati) {
		this.pubblicati = pubblicati;
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
