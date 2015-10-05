package com.bolo.photoshooters.web;


import java.io.Serializable;
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
import javax.faces.component.UIInput;
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
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;


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
	private Album nuovoAlbum = new Album();
	private List<Album> albumList = new ArrayList<Album>();
	
//	List<String> fotos = new ArrayList<String>();
	
//	public class Bean implements Serializable {
//
//		   /**
//		 * nel file html : styleClass="#{bean.inputValid?'':'inputInvalid'}"
//		 */
//			private static final long serialVersionUID = 1L;
//			private String text; 
//			public String getText() {
//				return text;
//			}
//			public void setText(String text) {
//				this.text = text;
//			}
//		    public boolean isInputValid (){
//		      FacesContext context = FacesContext.getCurrentInstance();
//		      UIInput input = (UIInput)context.getViewRoot().findComponent(":form:input");
//		      return input.isValid();
//		   }
//		}


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
		boolean et�Inserita = false;
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
		if (cercaUtente.getEt�()>0) {
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " TIMESTAMPDIFF(YEAR,u.dataMember,CURDATE()) <1 ";
			et�Inserita = true;
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
//		if(et�Inserita){
//			q.setParameter("eta", cercaUtente.getEt�());
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
	}
	
	
	private List<PerPhotoswipe> pswp = new ArrayList<UtenteBean.PerPhotoswipe>();
	private String pswpS = "";
	
	public String getPswpS() {
		pswpS = "[";
		for (PerPhotoswipe p : pswp) {
			pswpS += "{";
			pswpS += "src:'"+p.getSrc()+"',";
			pswpS += "w:"+p.getW()+",";
			pswpS += "h:"+p.getH()+"";
			pswpS += "},";
		}
		pswpS=pswpS.substring(0,pswpS.length()-1);
		pswpS += "]";
		System.out.println("PSWP=="+pswpS+"GET");
		return pswpS;
	}

	public void setPswpS(String pswpS) {
		this.pswpS = pswpS;
	}

	public void visualizzaFotos(int albumId){
		EntityManager em = EMF.createEntityManager();

		String hql = "from Foto f where f.album.id=:n";
		Query q = em.createQuery(hql, Foto.class);
		q.setParameter("n", albumId);

		risultatoFotos = (List<Foto>) q.getResultList();

		if(risultatoFotos!=null ){
			visualizzaAlbum(albumId);
			pswp.clear();
			for (Foto f : risultatoFotos) {
				PerPhotoswipe pp = new PerPhotoswipe();
				pp.setSrc("/lil?path="+utente.getUsername()+"/"+getRisultatoAlbum().getTitolo()+"/"+f.getNomeFileFoto());
				pp.setH(f.getAltezzaFoto());
				pp.setW(f.getLarghezzaFoto());
				pswp.add(pp);
				System.out.println("PHOTOSWIPE"+pp.getSrc()+pp.getH()+pp.getW()+getRisultatoAlbum().getTitolo());
			}
			System.out.println("PSWP=="+pswpS+"FINE");
			contentBean.setContent("visualizzaAlbum3.xhtml");
			contentBean.setMessaggio(null);
			setAlbumId(albumId);
//			visualizzaAlbum(albumId);

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
			contentBean.setContent("profilo2.xhtml");
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


	public int calcolaEt�(Date dataNascita) {
		Date currentDate = new Date(); // current date
        Calendar birth = new GregorianCalendar();
        Calendar today = new GregorianCalendar();
        int et� = 0;
        int compiuti = 0;
        today.setTime(currentDate);

        if (dataNascita!=null){
            birth.setTime(dataNascita);
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                compiuti = -1;
            }
            
            et� = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)+ compiuti;

            return et�;
        }
		return 0;    
	}
	
	
	public List<String> suggerisciCitt� (String citta) throws Exception{
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAg7ZoORXv7d2eMGKb-pB7_QZReIPiIzxw");

		GeocodingResult[] results =  GeocodingApi.geocode(context, citta).await();
		List<String> suggerimenti = new ArrayList<String>();
		
		for (GeocodingResult geocodingResult : results) {
//			System.out.println(geocodingResult.formattedAddress);	
			suggerimenti.add(geocodingResult.formattedAddress);
			utente.setCitt�(geocodingResult.formattedAddress);
			System.out.println("XXXX"+results[0].formattedAddress);
		}

	return suggerimenti;
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