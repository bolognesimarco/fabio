package com.bolo.photoshooters.web;

import javax.faces.context.FacesContext;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.primefaces.event.SelectEvent;

import com.bolo.photo.web.entity.Album;
import com.bolo.photo.web.entity.Annuncio;
import com.bolo.photo.web.entity.EmailDaInviare;
import com.bolo.photo.web.entity.Esperienza;
import com.bolo.photo.web.entity.Foto;
import com.bolo.photo.web.entity.Messaggio;
import com.bolo.photo.web.entity.Post;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.Thread;
import com.bolo.photo.web.entity.TipoLavoro;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photo.web.entity.Voto;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.util.IndirectListSorter;
import com.bolo.photoshooters.util.PostsComparator;
import com.bolo.photoshooters.vo.CercaUtenteVO;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;


@ManagedBean
@SessionScoped
public class UtenteBean {

	private Utente utente;
	private CercaUtenteVO cercaUtente = new CercaUtenteVO();
	List<Utente> risultato = new ArrayList<Utente>();
    List<TipoLavoro> risultatoLavori = new ArrayList<TipoLavoro>();
	private ServiziComuni serv = new ServiziComuniImpl();
	private String avatarDefault = "avatarDefault.svg"; 
	private String region ="";
	List<Foto> collaborazioniFoto = new ArrayList<Foto>();
	List<Utente> collaborazioniUtenti = new ArrayList<Utente>();
	List<Foto> fotosPreferite = new ArrayList<Foto>();	
	List<Utente> seguitoDaUtenti = new ArrayList<Utente>();
	List<Utente> followers = new ArrayList<Utente>();
	private boolean collaboratoUtente = false;
	private boolean tutteRegioni = false;
	List<Post> postsUtenteProponente = new ArrayList<Post>();
	List<Post> postsRispostiDaUtente = new ArrayList<Post>();	


	public void cercaUtenti(){		
		EntityManager em = EMF.createEntityManager();
		//String hql = "from Utente u where u.name=:n and u.username like :un and u.esperienza=:esp and u.tipoUtente.id=:tipout ";
		String hqlstart = "from Utente u ";
		String hqlcerca = "";
		String hql = "";
		
		boolean utenteLoggato = false;
		boolean nomeInserito = false;
		boolean usernameInserito = false;
		boolean esperienzaInserito = false;
		boolean utenteInserito = false;
		boolean sessoInserito = false;
		boolean utenteOnline = false;
		boolean et‡Inserita = false;
		int i = 0;
		
		if(utente!=null){
			hqlcerca += " u.id<>:idUt ";
			utenteLoggato = true;
			i++;
		}
		if (cercaUtente.getName()!="") {
			if (i>0){
				hqlcerca += "and";
			}
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
			hqlcerca += " u.esperienza=:esp or u.esperienza is null ";
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
		
		//ricerca x fascia et‡ inserita
		Calendar nAnniFa = null;
		Calendar nAnniFaMenoUno = null;
		int minEt‡=0;
		int maxEt‡=0;
//		System.out.println("=====GETETA''''''''''''''''''''''"+cercaUtente.getEt‡());
		if (cercaUtente.getEt‡()>-1) {
			switch (cercaUtente.getEt‡()){
			case 1: //<18
				minEt‡=1;
				maxEt‡=17;
				break;		
			case 2: //18-21
				minEt‡=19;
				maxEt‡=21;
				break;
			case 3: //22-25
				minEt‡=23;
				maxEt‡=25;
				break;
			case 4: //26-30
				minEt‡=27;
				maxEt‡=30;
				break;
			case 5: //31-40
				minEt‡=32;
				maxEt‡=40;
				break;
			case 6: //>40
				minEt‡=42;
				maxEt‡=99;
				break;		
			default:
				break;
			}
//			System.out.println("minETA'-maxETA'::::"+minEt‡+"--"+maxEt‡+"I:"+i);
			nAnniFa = Calendar.getInstance();
			nAnniFa.add(Calendar.YEAR, -minEt‡);
			
			nAnniFaMenoUno = Calendar.getInstance();
			nAnniFaMenoUno.add(Calendar.YEAR, -maxEt‡-1);
			
			if (i>0){
				hqlcerca += "and";
			}
			hqlcerca += " (u.dataNascita between :nAnniFaMenoUno and :nAnniFa) or u.dataNascita is null ";
			et‡Inserita = true;
			i++;
//			System.out.println("nANNI+FA'::::"+nAnniFa);
//			System.out.println("nANNImENOUNO+FA'::::"+nAnniFaMenoUno);
		}
		
		if (i>0){
			hql = hqlstart + "where" + hqlcerca; 
		}
		else {
			hql = hqlstart; 
		}
			
		Query q = em.createQuery(hql, Utente.class);
		if(utenteLoggato){
			q.setParameter("idUt",utente.getId());
		}
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
		if(et‡Inserita){
			q.setParameter("nAnniFaMenoUno", nAnniFaMenoUno.getTime());
			q.setParameter("nAnniFa", nAnniFa.getTime());
			System.out.println("nANNIFAMENOUNO"+nAnniFaMenoUno.getTime()+", nANNIFA:"+nAnniFa.getTime());
		}

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

					if(u.getRegioniitaliane().contains(RegioneItaliana.valueOf(reg.getRegioneitaliana())) || u.getRegioniitaliane().size()==0){
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


	public List<Utente> ultimiUtentiIscritti () {
		System.out.println("ultimiutentiiscritti");
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
		.createQuery("from Utente u order by u.dataIscrizione desc")
		.setMaxResults(2)
		.getResultList();
		return utenti;
	}
	
	
	public List<Foto> ultimeFotoInserite () {
		System.out.println("ultimeFotoIserite");
		EntityManager em = EMF.createEntityManager();
		List<Foto> fotos = em
		.createQuery("from Foto f order by f.dataFoto desc")
		.setMaxResults(2)
		.getResultList();
		System.out.println("ultimeFotoIserite size"+fotos.size());
		return fotos;
	}
	
	
	public List<Foto> classificaFoto () {
		System.out.println("classificaFoto");
		EntityManager em = EMF.createEntityManager();
		List<Foto> fotos = em
		.createQuery("from Foto f order by f.mediaVoti desc")
		.setMaxResults(3)
		.getResultList();
		return fotos;
	}
	
	
	public void utenteTrovatoId (int id){
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
		.createQuery("from Utente u where u.id=:idutente")
		.setParameter("idutente", id)
		.getResultList();
		if(utenti!=null && utenti.size()>0){
//			se non sono loggato
			if (utente==null){
				System.out.println("utente==null - username trovato= "+utenti.get(0).getUsername());
				cercaUtente.setUtente(utenti.get(0));
				contentBean.setContent("utenteTrovato3.xhtml");
			}  
			else { //utente trovato non sono io - aggiungo visita
				if(utenti.get(0).getId()!=utente.getId()) {
					aggiungiVisitaUtente(utenti.get(0));
					cercaUtente.setUtente(utenti.get(0));
					contentBean.setContent("utenteTrovato3.xhtml");
					collaboratoUtente = false;
					for (Utente ut : cercaUtente.getUtente().getCollaboratori()) {
						if(ut.getId()==utente.getId()) {
							collaboratoUtente = true;
						}
					}
				} 
				else { //utente trovato sono io
					contentBean.setContent("profilo2.xhtml");
					System.out.println("sono iooooo");
				}
			}
		}
		else{
			System.out.println("errore id utente non trovato!");
		}
	}
	
	
	public boolean utenteVisitato (Utente ut) {
		for (Utente u : ut.getUtentiVisitatori()) {
			if (u.getId()==utente.getId()) {
				return true;
			}
		}
		return false;
	}
	
	
	public void aggiungiVisitaUtente (Utente ut) {
		if (ut.getVisite()<2000000000) {
			int v = ut.getVisite()+1;
			ut.setVisite(v);
		}
		if (!utenteVisitato(ut)){
			if (ut.getUtentiVisitatori().size()<100) {
				ut.getUtentiVisitatori().add(utente);
			} else {
				ut.getUtentiVisitatori().remove(0);
				ut.getUtentiVisitatori().add(utente);
			}
		}
		try {
			serv.merge(ut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
//	usato da EntityConverter
	public Utente cercaUtente (String username){
		EntityManager em = EMF.createEntityManager();
		
		List<Utente> utenti = em
		.createQuery("from Utente u where u.username=:user")
		.setParameter("user", username)
		.getResultList();
		if(utenti!=null && utenti.size()>0){
			return utenti.get(0);
		}else{
			contentBean.setMessaggio("errore utente non trovato!");
			System.out.println("UTENTE NON TROVATOOOOO - username="+username);
			return null;
		}
	}
	
	
	public void aggiornaProfilo() {
		
		try {
//			if (utente.getCitt‡()!=null) {
//				region = "";
//				suggerisciCitt‡(utente.getCitt‡());
//				System.out.println("aggiorna CITTA'===="+utente.getCitt‡());
				if (region!="" && !controllaRegione(region)) {
					System.out.println("AAGIORNAPROFILO REGIONE===="+region);
					utente.getRegioniitaliane().add(RegioneItaliana.valueOf(region));
				}
				if (region=="" && utente.getCitt‡()!="") {
					suggerisciCitt‡(utente.getCitt‡());
					if (!controllaRegione(region)) {
						System.out.println("AAGIORNAPROFILO22222 REGIONE===="+region);
						utente.getRegioniitaliane().add(RegioneItaliana.valueOf(region));

					}
				}
//				}
			serv.merge(utente);
			String mm = "PROFILo AGGIORNATo";
			contentBean.setMessaggio(mm);
			contentBean.setContent("profilo2.xhtml");
			region = "";
		} catch (Exception e) {
			e.printStackTrace();
			String mm = e.getMessage()+" ERRORe";
			contentBean.setMessaggio(mm);
		}
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

	
	public double calcolaMediaVotiFoto (List<Voto> listaVoti) {
		if(listaVoti.size()>0) {
		double numeroVoti=0;
		double sommaVoti=0;
		for (Voto v : listaVoti) {
			sommaVoti += v.getScore();
			numeroVoti++;	
		}
		System.out.println("NUMERO--VOTI---:" + numeroVoti);
		System.out.println("SOMMA--VOTI---:" + sommaVoti);
		double mediaVoti = sommaVoti/numeroVoti;
		System.out.println("MEDIA--VOTI---:" + mediaVoti);
		mediaVoti = Double.parseDouble(new DecimalFormat("##.##").format(mediaVoti));
		return mediaVoti;
		}
		return 0;
	}

	
	public int calcolaEt‡(Date dataNascita) {
		Date currentDate = new Date(); // current date
        Calendar birth = new GregorianCalendar();
        Calendar today = new GregorianCalendar();
        int et‡ = 0;
        int compiuti = 0;
        today.setTime(currentDate);
        if (dataNascita!=null){
            birth.setTime(dataNascita);
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                compiuti = -1;
            }          
            et‡ = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)+ compiuti;
            return et‡;
        }
		return 0;    
	}

	
//	public List<String> convertiUtenteInUsername (List<Utente> utenti) {
//		List <String> usernames = new ArrayList<String>();
//		if (utenti==null){
//			return null;
//		}
//		for (Utente ut : utenti) {
//			usernames.add(ut.getUsername());
//		}
//		return usernames;
//	}
	
	
	public List<Utente> suggerisciUtente (String username) {		
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
		.createQuery("from Utente u where u.username like :user")
		.setParameter("user", username+"%")
		.getResultList();
		
		if(utenti!=null && utenti.size()>0){
			for (Utente ut : utenti) {
				System.out.println("UTENTI LIKE"+ut.getUsername());
			}			
		}else{
			System.out.println("errore utente non trovato!");
		}
		return utenti;
	}
	

	public List<Utente> suggerisciUtenteTranneLoggato (String username) {			
		EntityManager em = EMF.createEntityManager();
		List<Utente> utenti = em
		.createQuery("from Utente u where u.username like :user and u.id<>:idUt")
		.setParameter("user", username+"%")
		.setParameter("idUt", utente.getId())
		.getResultList();
//		for (Utente ut : utenti) {
//			if (ut.getId()==handleSelect(username))
//		}
		return utenti;
	}
	
	
	 public int handleSelect(SelectEvent event) {
		    Utente p=(Utente)event.getObject();
		    return p.getId();
		}
	
	
	public List<String> suggerisciCitt‡ (String cit) throws Exception{
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAg7ZoORXv7d2eMGKb-pB7_QZReIPiIzxw");
		
		List<String> suggerimenti = new ArrayList<String>();
		
		System.out.println("GEOCODE context1 :"+context+" - CIT 1"+cit);
		if (utente.getCitt‡().toString()!="") {
			
			System.out.println("GEOCODE context2 :"+context+" - CITTA'2"+utente.getCitt‡());
			GeocodingResult[] results =  GeocodingApi.geocode(context, cit).await();

			
			for (GeocodingResult geocodingResult : results) {
		
				suggerimenti.add(geocodingResult.formattedAddress);
				utente.setCitt‡(geocodingResult.formattedAddress);
				if(geocodingResult.addressComponents.length>=3){
					System.out.println("SUGGERISCIREGIONEEE=="+geocodingResult.addressComponents[3].longName);	
				}
				System.out.println("SUGGERISCICITTAAAAA=="+results[0].formattedAddress);
				if(geocodingResult.addressComponents.length>=3){		
					//le regioni le trova in inglese
					switch (geocodingResult.addressComponents[3].longName) {
					case "Abruzzo":
						region = "ABRUZZo";
						break;
					case "Basilicata":
						region = "BASILICATa";
						break;
					case "Calabria":
						region = "CALABRIa";
						break;
					case "Campania":
						region = "CAMPANIa";
						break;
					case "Emilia-Romagna":
						region = "EMILIa_ROMAGNa";
						break;
					case "Friuli-Venezia Giulia":
						region = "FRIULi_VENEZIa_GIULIa";
						break;
					case "Lazio":
						region = "LAZIo";
						break;
					case "Liguria":
						region = "LIGURIa";
						break;
					case "Lombardy":
						region = "LOMBARDIa";
						break;
					case "Marche":
						region = "MARCHe";
						break;
					case "Molise":
						region = "MOLISe";
						break;
					case "Piedmont":
						region = "PIEMONTe";
						break;
					case "Apulia":
						region = "PUGLIa";
						break;
					case "Sardegna":
						region = "SARDEGNa";
						break;
					case "Sicilia":
						region = "SICILIa";
						break;
					case "Tuscany":
						region = "TOSCANa";
						break;
					case "Trentino-Alto Adige":
						region = "TRENTINo_ALTo_ADIGe";
						break;
					case "Umbria":
						region = "UMBRIa";
						break;
					case "Valle d'Aosta":
						region = "VAl_dAOSTa";
						break;
					case "Veneto":
						region = "VENETo";
						break;		
					default: 
						region = "ESTERo";
						break;
					}			
					System.out.println("SUGGERISCIREGIONEEE22=="+region);
				}
			}	
		} 
		return suggerimenti;		
	

	}
	

	public boolean controllaRegione (String reg) {
		for (RegioneItaliana regUtente : utente.getRegioniitaliane()) {
			if (regUtente.toString().equals(reg)) {
				return true;
			}
		}
		return false;
	}
	
	
	public int tipoMembershipUtente () {
		if (utente==null) {
			System.out.println("membership =0");
			return 0;
		}
		System.out.println("membership !!!=0");
		System.out.println("membership ="+utente.getMemberships().get(utente.getMemberships().size()-1).getTipoMembership().getId());
		return utente.getMemberships().get(utente.getMemberships().size()-1).getTipoMembership().getId();
	}

	
	public void selezionaTutteRegioniCercate() {
		if (cercaUtente.isTutteRegioni()) {
			cercaUtente.getRegioniitaliane().clear();
			List<RegioneItaliana> List =
	                new ArrayList<RegioneItaliana>(EnumSet.allOf(RegioneItaliana.class));
			cercaUtente.setRegioniitaliane(List);
		} else {
			cercaUtente.getRegioniitaliane().clear();
		}
	}
	
	
	public void selezionaTutteRegioniProfilo() {
		if (isTutteRegioni()) {
			utente.getRegioniitaliane().clear();
			List<RegioneItaliana> List =
	                new ArrayList<RegioneItaliana>(EnumSet.allOf(RegioneItaliana.class));
			utente.setRegioniitaliane(List);
		} else {
			utente.getRegioniitaliane().clear();
		}
	}
	
	
	public void selezionaTutteRegioni(boolean cerca, List<RegioneItaliana> regions) {
		if (cerca) {
			regions.clear();
			List<RegioneItaliana> List =
            new ArrayList<RegioneItaliana>(EnumSet.allOf(RegioneItaliana.class));
//			regions = List;
			cercaUtente.setRegioniitaliane(List);
		} else {
			regions.clear();
		}
	}
	
	public void cercaCollaborazioniUtente (Utente utente) {
		System.out.println("cercaCollaborazioniUtenteFoto startttt----");
		EntityManager em = EMF.createEntityManager();
		collaborazioniFoto = em
		.createQuery("from Foto f inner join f.collaboratori c where c.id=:idUt")
		.setParameter("idUt", utente.getId())
		.getResultList();
		contentBean.setContent("collaborazioniUtente.xhtml");
		System.out.println("size"+collaborazioniFoto.size());
		collaborazioniUtenti = utente.getCollaboratori();
		System.out.println("size colla utente"+collaborazioniUtenti.size());
	}
	
	
	public void cercaPreferitiSeguitiDaUtente () {
		followers.clear();
		cercaFollowersUtente(utente);
		contentBean.setContent("preferitiUtente.xhtml");
	}
	
	
	public void cercaPreferentiFollowerUtenteTrovato (Utente ut) {
		EntityManager em = EMF.createEntityManager();
		fotosPreferite = em
		.createQuery("from Foto f where f.pubblicatore.id=:idU and f.utentiChePreferisconoFoto is not empty")
		.setParameter("idU", ut.getId())
		.getResultList();
//		em.close();
		seguitoDaUtenti = em
		.createQuery("from Utente u inner join u.utentiSeguiti us on us.id=:idUt")
		.setParameter("idUt", ut.getId())
		.getResultList();		
		contentBean.setContent("preferitiUtenteTrovato.xhtml");
	}

	
	public void visualizzaPostsUtente() {
		postsUtenteProponente.clear();
		postsRispostiDaUtente.clear();
//		utente.getPostsSeguiti().
		for (Post p : utente.getPostsPartecipati()) {
			if (p.getProponente().getId()==utente.getId()) {
				postsUtenteProponente.add(p);
			} else {
				postsRispostiDaUtente.add(p);
			}
		}
		ordinaPostPerData(postsUtenteProponente);
		ordinaPostPerData(postsRispostiDaUtente);
		ordinaPostPerData(utente.getPostsSeguiti());
		contentBean.setContent("postsUtente.xhtml");
	}
	
	IndirectListSorter<Post> postsSorter = new IndirectListSorter<Post>();
	
	public void ordinaPostPerData(List<Post> listPosts ) {	 
		System.out.println("ORDINA POSTS:#"+listPosts.size());
		//ordina per ultimo invio/ricezione
		postsSorter.sortIndirectList(listPosts, new PostsComparator());
	}


	public void seguiUtente(Utente u) {
		if(!utenteSeguito(u)) {
			utente.getUtentiSeguiti().add(u);
			try {
				if(u.isMailNuovoFollower()){
					EmailDaInviare email = new EmailDaInviare();
					email.setTipoEmail(5);
					email.setUtenteSender(utente);
					email.setEmail(u.getEmail());
					serv.persist(email);
	//				MailSender.sendNuovoFollowerMail(u.getEmail(), utente.getUsername());
				}
				System.out.println("seguiiiiiii");
				serv.merge(utente);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void nonSeguiUtente(Utente u) {
		Iterator<Utente> it = utente.getUtentiSeguiti().iterator();
		while(it.hasNext()) {
			Utente ut = it.next();
			if(ut.getId()==u.getId()) {
				it.remove();
			}
		}
		try {
			System.out.println("non seguiiiiiii");
			serv.merge(utente);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean utenteSeguito(Utente u) {
		if (utente!=null){
			for (Utente ut : utente.getUtentiSeguiti()) {
				if (u.getId()==ut.getId()) {
//					System.out.println("utente seguito = TRUEEEEEE");
					return true;
				}	
			}
//			System.out.println("utente seguito = FALSEEEEE");
			return false;
		}
		return false;
	}
	
	
	public List<Utente> cercaFollowersUtente (Utente ut) {
		EntityManager em = EMF.createEntityManager();
		List <Utente> followers = em
		.createQuery("from Utente u inner join u.utentiSeguiti us on us.id=:idUt")
		.setParameter("idUt", ut.getId())
		.getResultList();		
		return followers;
	}
	
	
	public void sessioneDistrutta () {
		contentBean.setContent("homePage.xhtml");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("menuutenteform");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("headerform");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("patternguestform");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
	}
	
	
	
	
	
	
	
	
	
	//************GETTERS & SETTERS*******************



	
	
	public List<Utente> getCollaborazioniUtenti() {
		return collaborazioniUtenti;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean isTutteRegioni() {
		return tutteRegioni;
	}

	public void setTutteRegioni(boolean tutteRegioni) {
		this.tutteRegioni = tutteRegioni;
	}

	public boolean isCollaboratoUtente() {
		return collaboratoUtente;
	}

	public void setCollaboratoUtente(boolean collaboratoUtente) {
		this.collaboratoUtente = collaboratoUtente;
	}

	public List<Utente> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Utente> followers) {
		this.followers = followers;
	}

	public List<Utente> getSeguitoDaUtenti() {
		return seguitoDaUtenti;
	}

	public void setSeguitoDaUtenti(List<Utente> seguitoDaUtenti) {
		this.seguitoDaUtenti = seguitoDaUtenti;
	}

	public void setCollaborazioniUtenti(List<Utente> collaborazioniUtenti) {
		this.collaborazioniUtenti = collaborazioniUtenti;
	}

	public List<Foto> getFotosPreferite() {
		return fotosPreferite;
	}

	public void setFotosPreferite(List<Foto> fotosPreferite) {
		this.fotosPreferite = fotosPreferite;
	}

	public List<Foto> getCollaborazioniFoto() {
		return collaborazioniFoto;
	}
	
	public void setCollaborazioniFoto(List<Foto> collaborazioniFoto) {
		this.collaborazioniFoto = collaborazioniFoto;
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

	public List<Post> getPostsUtenteProponente() {
		return postsUtenteProponente;
	}

	public void setPostsUtenteProponente(List<Post> postsUtenteProponente) {
		this.postsUtenteProponente = postsUtenteProponente;
	}

	public List<Post> getPostsRispostiDaUtente() {
		return postsRispostiDaUtente;
	}

	public void setPostsRispostiDaUtente(List<Post> postsRispostiDaUtente) {
		this.postsRispostiDaUtente = postsRispostiDaUtente;
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