package com.bolo.photoshooters.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.bolo.photo.web.entity.Esperienza;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoLavoro;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.vo.CercaUtenteVO;


@ManagedBean
@SessionScoped
public class UtenteBean {

	private Utente utente;
	private CercaUtenteVO cercaUtente = new CercaUtenteVO();
	List<Utente> risultato = new ArrayList<Utente>();
    List<TipoLavoro> risultatoLavori = new ArrayList<TipoLavoro>();
//	private SelectItem[] risultatoLavori;
	private ServiziComuni serv = new ServiziComuniImpl();
//	private String username;
//	private String name;


	public void cercaUtenti(){
		
		EntityManager em = EMF.createEntityManager();
		//String hql = "from Utente u where u.name=:n and u.username=:un and u.esperienza=:esp and u.tipoUtente.id=:tipout ";
		String hqlstart = "from Utente u ";
		String hqlcerca = "";
		String hql = "";

		boolean nomeInserito = false;
		boolean usernameInserito = false;
		boolean esperienzaInserito = false;
		boolean utenteInserito = false;
		boolean lavoroInserito = false;
		int i = 0;
		
		if (cercaUtente.getName()!="") {
			hqlcerca += " u.name=:n ";
			nomeInserito = true;
			i++;
		}
		if (cercaUtente.getUsername()!="") {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " u.username like :un ";
			usernameInserito = true;
			i++;
		}
		if (cercaUtente.getEsperienza()!=null) {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " u.esperienza=:esp ";
			esperienzaInserito = true;
			i++;
		}
		if (cercaUtente.getTipoUtente()>-1) {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " u.tipoUtente.id=:tipout ";
			utenteInserito = true;
			i++;
		}
		if (cercaUtente.getTipiLavoro()!=null) {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " u.tipiLavoro in (:tiplav) ";
			lavoroInserito = true;
			i++;
		}
		if (i>0){
			hql = hqlstart + "where" + hqlcerca; 
		}
		else {
			hql = hqlstart; 
		}
			
		Query q = em.createQuery(hql, Utente.class);
		if(nomeInserito){
			q.setParameter("n",cercaUtente.getName());
		}
		if(usernameInserito){
			q.setParameter("un", "%" + cercaUtente.getUsername() + "%");
		}
		if(esperienzaInserito){
			q.setParameter("esp", cercaUtente.getEsperienza());
		}
		if(utenteInserito){
			q.setParameter("tipout", cercaUtente.getTipoUtente());
		}
		List<TipoLavoro> tipiLavoroSelezionati = new ArrayList<TipoLavoro>();
		if(lavoroInserito){
			q.setParameter("tiplav", cercaUtente.getTipiLavoro());
		}
		risultato = q.getResultList();
		System.out.println("================CCC==========="+risultato.size());
		
		//selezione per regioni - se selezionate cioè <> da []
		if (cercaUtente.getRegioniitaliane().toString()!="[]") {
			System.out.println("XXXXXXXXXXXXX"+cercaUtente.getRegioniitaliane().toString());			
			Iterator<Utente> iter = risultato.iterator();
	
			while(iter.hasNext()){
				Utente u = iter.next();
				boolean trovato = false;
				
				for (RegioneItaliana regita : u.getRegioniitaliane()) {
					System.out.println("=====BBBBBBBB==============="+regita.toString());	
				}
							
				for (RegioneItaliana reg : cercaUtente.getRegioniitaliane()) {
					System.out.println("=======AAA================"+reg.getRegioneitaliana());
					
					if(u.getRegioniitaliane().contains(RegioneItaliana.valueOf(reg.getRegioneitaliana()))){
						System.out.println("=================================LUI SI:"+u.getName());
						trovato = true;
						break;
					}
				}
				if(!trovato){
					System.out.println("=================================lUI NO:"+u.getName());
					iter.remove();
				}
				trovato = false;
			}
		}
		//contentBean.setContent("risultatiCerca.xhtml");	
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
	
	
	public List<Utente> getRisultato() {
		return risultato;
	}

	public void setRisultato(List<Utente> risultato) {
		this.risultato = risultato;
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
	
	
	public void lavoriPerTipoUtente() {
		if (cercaUtente.getTipoUtente()!=-1){
			EntityManager em = EMF.createEntityManager();
			String hql = "select t from TipoLavoro t inner join t.tipiUtente tl where tl.id=:n ";
			Query q = em.createQuery(hql, TipoLavoro.class);
			q.setParameter("n",cercaUtente.getTipoUtente());
			risultatoLavori = q.getResultList();
			System.out.println("===========XX=============XXX:" + risultatoLavori.toString());
		}
		
	}
	
	
	public List<TipoLavoro> getRisultatoLavori() {
		return risultatoLavori;

	}


	public void setRisultatoLavori(List<TipoLavoro> risultatoLavori) {
		this.risultatoLavori = risultatoLavori;
	}


	public void setTipiLavoroId(List<String> tipilavoro) throws NumberFormatException, Exception {
		utente.getTipiLavoro().clear();
		for (String id : tipilavoro) {
			utente.getTipiLavoro().add(serv.getReference(TipoLavoro.class, Integer.valueOf(id)));
		}
	}
	public void setCercaTipiLavoroId(List<String> tipilavoro) throws NumberFormatException, Exception {
		cercaUtente.getTipiLavoro().clear();
		for (String id : tipilavoro) {
			cercaUtente.getTipiLavoro().add(serv.getReference(TipoLavoro.class, Integer.valueOf(id)));
		}
	}
	
	public List<Integer> getTipiLavoroId() {
		List<Integer> darit = new ArrayList<Integer>();
		for (TipoLavoro t:utente.getTipiLavoro()){
			darit.add(t.getId());
		}
		return darit;
	}
	public List<Integer> getCercaTipiLavoroId() {
		List<Integer> darit = new ArrayList<Integer>();
		for (TipoLavoro t:cercaUtente.getTipiLavoro()){
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