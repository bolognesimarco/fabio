package com.bolo.photoshooters.web;


import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
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
import com.bolo.photo.web.entity.Foto;
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
    Album risultatoAlbum = new Album();
    List<Foto> risultatoFotos = new ArrayList<Foto>();
    Foto risultatoFoto = new Foto();
	private ServiziComuni serv = new ServiziComuniImpl();
	private String avatarDefault = "avatarDefault.svg"; 
	private String newAlbumName ="";
	private String albumVisualizzato ="";
	private Foto nuovaFoto = new Foto();
	private String newFotoName ="";
	private Integer albumId;
	private Integer fotoId;
	List<String> fotos = new ArrayList<String>();
	
	
	public Integer getFotoId() {
		return fotoId;
	}

	public void setFotoId(Integer fotoId) {
		this.fotoId = fotoId;
	}

	public String getAlbumVisualizzato() {
		return albumVisualizzato;
	}

	public void setAlbumVisualizzato(String albumVisualizzato) {
		this.albumVisualizzato = albumVisualizzato;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public Foto getNuovaFoto() {
		return nuovaFoto;
	}

	public void setNuovaFoto(Foto nuovaFoto) {
		this.nuovaFoto = nuovaFoto;
	}

	public String getNewFotoName() {
		return newFotoName;
	}

	public void setNewFotoName(String newFotoName) {
		this.newFotoName = newFotoName;
	}

	private Album nuovoAlbum = new Album();
	private List<Album> albumList = new ArrayList<Album>();

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
		boolean etàInserita = false;
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
		if (cercaUtente.getEtà()>0) {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " TIMESTAMPDIFF(YEAR,u.dataMember,CURDATE()) <1 ";
			etàInserita = true;
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
//		if(etàInserita){
//			q.setParameter("eta", cercaUtente.getEtà());
//		}
//		ORDINAMENTO PRIMA DELLA QUERY
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

	public void visualizzaAlbum(int albumId){
		EntityManager em = EMF.createEntityManager();

		String hql = "from Album a where a.id=:n";
		Query q = em.createQuery(hql, Album.class);
		q.setParameter("n", albumId);
		
		risultatoAlbum = (Album) q.getResultList().get(0);
		
//		System.out.println("PPPPPPPPPPPPPPP");
//		System.out.println(risultatoAlbum.getFotos().size());
//		System.out.println(risultatoAlbum.getFotos().get(0).getTitolo());
//		if(risultatoAlbum!=null){
//
//			contentBean.setContent("visualizzaAlbum.xhtml");
//			contentBean.setMessaggio(null);
//			setAlbumId(albumId);
//			setAlbumVisualizzato(risultatoAlbum.getTitolo());
//		}else{
//			System.out.println("errore album non trovato!");
//		}
	}
	
	
	private List<PerPhotoswipe> pswp = new ArrayList<UtenteBean.PerPhotoswipe>();
	
	public void visualizzaFotos(int albumId){
		EntityManager em = EMF.createEntityManager();

		String hql = "from Foto f where f.album.id=:n";
		Query q = em.createQuery(hql, Foto.class);
		q.setParameter("n", albumId);

		risultatoFotos = (List<Foto>) q.getResultList();
		for (Foto f : risultatoFotos) {
			fotos.add(f.getNomeFileFoto());
			
			
			PerPhotoswipe pp = new PerPhotoswipe();
			pp.setSrc(f.getNomeFileFoto());
			pp.setH(f.getAltezzaFoto());
			pp.setW(f.getLarghezzaFoto());
			pswp.add(pp);
			System.out.println("PHOTOSWIPE"+pp.getSrc()+pp.getH()+pp.getW());
		}
//		System.out.println(risultatoAlbum.getFotos().get(0).getTitolo());
		if(risultatoFotos!=null ){

			contentBean.setContent("visualizzaAlbum3.xhtml");
			contentBean.setMessaggio(null);
			setAlbumId(albumId);
			visualizzaAlbum(albumId);
//			setAlbumVisualizzato(risultatoAlbum.getTitolo());
		}else{
			System.out.println("errore lista foto non trovata!");
		}
	}
	
	public List<PerPhotoswipe> getPswp() {
		return pswp;
	}

	public void setPswp(List<PerPhotoswipe> pswp) {
		this.pswp = pswp;
	}

	public class PerPhotoswipe{
		private String src;
		private int w;
		private int h;
		public String getSrc() {
			return src;
		}
		public void setSrc(String src) {
			this.src = src;
		}
		public int getW() {
			return w;
		}
		public void setW(int w) {
			this.w = w;
		}
		public int getH() {
			return h;
		}
		public void setH(int h) {
			this.h = h;
		}
		
		
	}
	
	public List<String> getFotos() {
		return fotos;
	}

	public void setFotos(List<String> fotos) {
		this.fotos = fotos;
	}

	public void visualizzaFoto(int fotoId){
		EntityManager em = EMF.createEntityManager();

		String hql = "from Foto f where f.id=:n";
		Query q = em.createQuery(hql, Foto.class);
		q.setParameter("n", fotoId);

		risultatoFoto = (Foto) q.getResultList().get(0);

//		System.out.println(risultatoAlbum.getFotos().get(0).getTitolo());
		if(risultatoFoto!=null ){

			contentBean.setContent("visualizzaFoto.xhtml");
			contentBean.setMessaggio(null);
//			setAlbumId(albumId);
//			visualizzaAlbum(albumId);
//			setAlbumVisualizzato(risultatoAlbum.getTitolo());
		}else{
			System.out.println("errore foto non trovata!");
		}
	}
	
	public Foto getRisultatoFoto() {
		return risultatoFoto;
	}

	public void setRisultatoFoto(Foto risultatoFoto) {
		this.risultatoFoto = risultatoFoto;
	}

	public List<Foto> getRisultatoFotos() {
		return risultatoFotos;
	}

	public void setRisultatoFotos(List<Foto> risultatoFotos) {
		this.risultatoFotos = risultatoFotos;
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
			System.out.println("errore utente non trovato!");
		}
	}
	
	
	public void aggiornaProfilo() {
		try {
			serv.merge(utente);
			String mm = "PROFILo AGGIORNATo";
			contentBean.setMessaggio(mm);
			contentBean.setContent("profilo.xhtml");
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


	public int calcolaEtà(Date dataNascita) {
		Date currentDate = new Date(); // current date
        Calendar birth = new GregorianCalendar();
        Calendar today = new GregorianCalendar();
        int età = 0;
        int compiuti = 0;
        today.setTime(currentDate);

        if (dataNascita!=null){
            birth.setTime(dataNascita);
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                compiuti = -1;
            }
            
            età = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)+ compiuti;

            return età;
        }
		return 0;
        
	}
	

	public Album getNuovoAlbum() {
		return nuovoAlbum;
	}


	public void setNuovoAlbum(Album nuovoAlbum) {
		this.nuovoAlbum = nuovoAlbum;
	}


	public List<Album> getAlbumList() {
		return albumList;
	}


	public void setAlbumList(List<Album> albumList) {
		this.albumList = albumList;
	}


	public String getNewAlbumName() {
		return newAlbumName;
	}


	public void setNewAlbumName(String newAlbumName) {
		this.newAlbumName = newAlbumName;
	}
	
	public String getAvatarDefault() {
		return avatarDefault;
	}

	public void setAvatarDefault(String avatarDefault) {
		this.avatarDefault = avatarDefault;
	}
	
	public List<Utente> getRisultato() {
		return risultato;
	}


	public Album getRisultatoAlbum() {
		return risultatoAlbum;
	}


	public void setRisultatoAlbum(Album risultatoAlbum) {
		this.risultatoAlbum = risultatoAlbum;
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