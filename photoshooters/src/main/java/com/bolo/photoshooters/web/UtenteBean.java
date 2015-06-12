package com.bolo.photoshooters.web;


import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.bolo.photo.web.entity.Esperienza;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoLavoro;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.vo.CercaUtenteVO;


@ManagedBean
@SessionScoped
public class UtenteBean {

	private Utente utente;
	private CercaUtenteVO cercaUtente;
	List<Utente> risultato = new ArrayList<Utente>();
	private ServiziComuni serv = new ServiziComuniImpl();
	private String username;
	private String name;

	public List<Utente> getRisultato() {
		return risultato;
	}

	public void setRisultato(List<Utente> risultato) {
		this.risultato = risultato;
	}

	public void cercaUtenti(){
		
		EntityManager em = EMF.createEntityManager();
		String hql = "from Utente u where u.name=:n and u.username=:un";
		Query q = em.createQuery(hql, Utente.class);
		q.setParameter("un", name);
		q.setParameter("n", username);
		
		risultato = q.getResultList();
		//contentBean.setContent("risultatiCerca.xhtml");	
		contentBean.setMessaggio("risultati"+name);
	}
	
	public void aggiornaProfilo() {
		
		try {
			serv.merge(utente);
			String mm = "PROFILo AGGIORNATo";
			contentBean.setMessaggio(mm);		
		} catch (Exception e) {
			e.printStackTrace();
	
		//	contentBean.setContent("profilo.xhtml");
			String mm = e.getMessage()+" ERRORe";
			contentBean.setMessaggio(mm);
		}
		
		//contentBean.setContent(null);
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
	}
	
//	public List<Utente> getRisultato() {
//		return risultato;
//	}
//
//	public void setRisultato(List<Utente> risultato) {
//		this.risultato = risultato;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Utente getUtente() {
		return utente;
	}

	public CercaUtenteVO getCercaUtente() {
		return cercaUtente;
	}

	public void setCercaUtente(CercaUtenteVO cercaUtente) {
		this.cercaUtente = cercaUtente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	public void rangeFill(List<Integer> campo2, int start, int end) {
	    for (int i = start; i <= end; i++) {
	    	campo2.add(new Integer(new Integer(i)));
	    }
	} 
	
	public void setTipiLavoroId(List<String> tipilavoro) throws NumberFormatException, Exception {
		utente.getTipiLavoro().clear();
		for (String id : tipilavoro) {
			utente.getTipiLavoro().add(serv.getReference(TipoLavoro.class, Integer.valueOf(id)));
		}
	}
	
	public List<Integer> getTipiLavoroId() {
		List<Integer> darit = new ArrayList<Integer>();
		for (TipoLavoro t:utente.getTipiLavoro()){
			darit.add(t.getId());
		}
		return darit;
	}
	
	public void fillSelectItems() {
		rangeFianchi.clear();
		rangeVita.clear();
		rangeAltezza.clear();
		rangePeso.clear();
		rangeSeno.clear();
		rangeTaglia.clear();
		rangeScarpe.clear();
		rangeTatuaggi.clear();
		rangePiercing.clear();
		rangeFill(rangeFianchi, 55, 160);
		rangeFill(rangeVita, 45, 130);
		rangeFill(rangeAltezza, 55, 160);
		rangeFill(rangePeso, 55, 160);
		rangeFill(rangeSeno, 55, 160);
		rangeFill(rangeTaglia, 30, 60);
		rangeFill(rangeScarpe, 30, 50);
		rangeFill(rangeTatuaggi, 0, 10);
		rangeFill(rangePiercing, 0, 10);
	}
		
	private List<Integer> rangeFianchi = new ArrayList<Integer>();
	private List<Integer> rangeVita = new ArrayList<Integer>();
	private List<Integer> rangeAltezza = new ArrayList<Integer>();
	private List<Integer> rangePeso = new ArrayList<Integer>();
	private List<Integer> rangeSeno = new ArrayList<Integer>();
	private List<Integer> rangeTaglia = new ArrayList<Integer>();
	private List<Integer> rangeScarpe = new ArrayList<Integer>();
	private List<Integer> rangeTatuaggi = new ArrayList<Integer>();
	private List<Integer> rangePiercing = new ArrayList<Integer>();
		
	public List<Integer> getRangeTatuaggi() {
		return rangeTatuaggi;
	}

	public void setRangeTatuaggi(List<Integer> rangeTatuaggi) {
		this.rangeTatuaggi = rangeTatuaggi;
	}

	public List<Integer> getRangePiercing() {
		return rangePiercing;
	}

	public void setRangePiercing(List<Integer> rangePiercing) {
		this.rangePiercing = rangePiercing;
	}

	public List<Integer> getRangeFianchi() {
		return rangeFianchi;
	}

	public void setRangeFianchi(List<Integer> rangeFianchi) {
		this.rangeFianchi = rangeFianchi;
	}

	public List<Integer> getRangeVita() {
		return rangeVita;
	}

	public void setRangeVita(List<Integer> rangeVita) {
		this.rangeVita = rangeVita;
	}

	public List<Integer> getRangeAltezza() {
		return rangeAltezza;
	}

	public void setRangeAltezza(List<Integer> rangeAltezza) {
		this.rangeAltezza = rangeAltezza;
	}

	public List<Integer> getRangePeso() {
		return rangePeso;
	}

	public void setRangePeso(List<Integer> rangePeso) {
		this.rangePeso = rangePeso;
	}

	public List<Integer> getRangeSeno() {
		return rangeSeno;
	}

	public void setRangeSeno(List<Integer> rangeSeno) {
		this.rangeSeno = rangeSeno;
	}

	public List<Integer> getRangeTaglia() {
		return rangeTaglia;
	}

	public void setRangeTaglia(List<Integer> rangeTaglia) {
		this.rangeTaglia = rangeTaglia;
	}

	public List<Integer> getRangeScarpe() {
		return rangeScarpe;
	}

	public void setRangeScarpe(List<Integer> rangeScarpe) {
		this.rangeScarpe = rangeScarpe;
	}

	public Esperienza[] getEsperienze() {
        return Esperienza.values();
	}
	
	public Sesso[] getSessi() {
		return Sesso.values();
	}
	
	public RegioneItaliana[] getRegioni() {
        return RegioneItaliana.values();
	}
	
	public ContentBean getContentBean() {
		return contentBean;
	}

	public void setContentBean(ContentBean contentBean) {
		this.contentBean = contentBean;
	}

	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;
}