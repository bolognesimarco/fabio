package com.bolo.photoshooters.web;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.bolo.photo.web.entity.Album;
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
    List<Album> risultatoAlbums = new ArrayList<Album>();
	private ServiziComuni serv = new ServiziComuniImpl();
	private String avatarDefault = "avatarDefault.svg"; 


	public void cercaUtenti(){		
		EntityManager em = EMF.createEntityManager();
		//String hql = "from Utente u where u.name=:n and u.username like :un and u.esperienza=:esp and u.tipoUtente.id=:tipout ";
		String hqlstart = "from Utente u ";
		String hqlcerca = "";
		String hql = "";

		boolean nomeInserito = false;
		boolean usernameInserito = false;
		boolean esperienzaInserito = false;
		boolean utenteInserito = false;
		boolean sessoInserito = false;
		boolean utenteOnline = false;
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
		if (cercaUtente.getSesso()!=null) {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " u.sesso=:sex ";
			sessoInserito = true;
			i++;
		}
		if (cercaUtente.isOnline()) {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " u.online = true ";
			utenteOnline = true;
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
		if(sessoInserito){
			q.setParameter("sex", cercaUtente.getSesso());
		}
		risultato = q.getResultList();
		System.out.println("================CCC==========="+risultato.size());
		
		//selezione per tipilavoro - se selezionati
		if (cercaUtente.getTipiLavoro().size()>0) {
			System.out.println("AAAAAAAAaaa"+cercaUtente.getTipiLavoro().toString());			
			Iterator<Utente> iter1 = risultato.iterator();
	
			while(iter1.hasNext()){
				Utente u = iter1.next();

				int j = 0;
				int h = 0;
				for (Integer tlavID : getCercaTipiLavoroId()) {
					System.out.println("=======LAVVVV================"+tlavID.toString());
					j++;
					for (TipoLavoro tl : u.getTipiLavoro()){
					
						if (tl.getId() == tlavID){
						System.out.println("======LAVORI========LUI SI:"+u.getName());
						h++;
						break;
						}
					}			
				}
				
				if(j!=h){
					System.out.println("===========LAVORI==============lUI NO:"+u.getName());
					iter1.remove();
				}
			}
		}

		//selezione per regioni - se selezionate		
		if (cercaUtente.getRegioniitaliane().size()>0) {
			System.out.println("XXXXXXXXXXXXX"+cercaUtente.getRegioniitaliane().toString());			
			Iterator<Utente> iter = risultato.iterator();
	
			while(iter.hasNext()){
				Utente u = iter.next();
				boolean trovato = false;
						
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
		ordinaPerAccessoIscrizione();
	}


	public String ordinaPerAccessoIscrizione() {
		 
		   if(cercaUtente.getLastOnlineIscritto()==1){
	 
			//ordina per ultimo accesso
			Collections.sort(risultato, new Comparator<Utente>() {
	 
				@Override
				public int compare(Utente u1, Utente u2) {
					return u2.getDataUltimoAccesso().compareTo(u1.getDataUltimoAccesso());
				}
			});
	 
		   }else{
	 
			//ordina per ultimo iscritto
			Collections.sort(risultato, new Comparator<Utente>() {

				@Override
				public int compare(Utente u1, Utente u2) {
					return u2.getDataIscrizione().compareTo(u1.getDataIscrizione());
				} 
			});
		   }
	 
		   return null;
		}

	
	
	public void utenteTrovato(String username){
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
		.createQuery("from Utente u where u.username=:user")
		.setParameter("user", username)
		.getResultList();
		if(utenti!=null && utenti.size()>0){
			cercaUtente.setUtente(utenti.get(0));
			contentBean.setContent("utenteTrovato.xhtml");
		}else{
			System.out.println("errore utenteTrovato!");
		}
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

//	public void albumsUtente() {
//		if (cercaUtente.getTipoUtente()!=-1){
//			EntityManager em = EMF.createEntityManager();
//			String hql = "select u from Utente where u.username=:n ";
//			Query q = em.createQuery(hql, Album.class);
//			q.setParameter("n",utente.getUsername());
//			risultatoAlbums = q.getResultList();
//			System.out.println("===========ALBUMS:" + risultatoAlbums.toString());
//		}
//		
//	}
	
	
	public String getAvatarDefault() {
		return avatarDefault;
	}

	public void setAvatarDefault(String avatarDefault) {
		this.avatarDefault = avatarDefault;
	}
	
	public List<Utente> getRisultato() {
		return risultato;
	}

	public List<Album> getRisultatoAlbums() {
		return risultatoAlbums;
	}

	public void setRisultatoAlbums(List<Album> risultatoAlbums) {
		this.risultatoAlbums = risultatoAlbums;
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
			if (Integer.valueOf(id)==0){
				break;
			}
			else {
				cercaUtente.getTipiLavoro().add(serv.getReference(TipoLavoro.class, Integer.valueOf(id)));
			}
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