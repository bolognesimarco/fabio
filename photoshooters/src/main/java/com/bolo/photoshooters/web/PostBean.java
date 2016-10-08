package com.bolo.photoshooters.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.bolo.photo.web.entity.Annuncio;
import com.bolo.photo.web.entity.Messaggio;
import com.bolo.photo.web.entity.Post;
import com.bolo.photo.web.entity.SuperPost;
import com.bolo.photo.web.entity.Thread;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.util.AnnunciComparator;
import com.bolo.photoshooters.util.IndirectListSorter;
import com.bolo.photoshooters.util.MessaggiComparator;
import com.bolo.photoshooters.util.PostsComparator;

@ManagedBean
@SessionScoped
public class PostBean {


//	private Messaggio messaggioAggiunto = new Messaggio();
//	List<Annuncio> annunciUtente = new ArrayList<Annuncio>();
//	List<Annuncio> annunciSito = new ArrayList<Annuncio>();
//	List<Annuncio> annunciRispostiDaUtente = new ArrayList<Annuncio>();
//	private Annuncio nuovoAnnuncio = new Annuncio();
//	private Messaggio messaggioNuovoAnnuncio = new Messaggio();
//	private Annuncio annuncioEsistente = new Annuncio();
//	private Annuncio annuncioPubblicato = new Annuncio();
//	private Annuncio annuncioAltrui = new Annuncio();
//	Messaggio messaggioRispostaAnnuncio = new Messaggio();
//	Thread threadAnnuncioPubblicato = new Thread();
//	Thread threadRispostaAnnuncio = new Thread();
//	List<Thread> threadsAnnunciConNuoviMessaggi = new ArrayList<Thread>();
//	private int nuoviMessaggiAnnunci = 0;
	
	
	private ServiziComuni serv = new ServiziComuniImpl();	
	private Post postUtente = new Post();
	List<Thread> threadsRispostePost = new ArrayList<Thread>();


	Thread threadInRisposta = new Thread();
//	int firstRow = 0;
	private List<Post> listaPostsGenerica = new ArrayList<Post>();
	private String titoloListaPostsGenerica = new String();

	private Thread threadGenerico = new Thread();
	private Messaggio messaggioInRisposta = new Messaggio();


	
	///
	private List<SuperPost> listaSuperPostsGenerica = new ArrayList<SuperPost>();
	private String titoloListaSuperPostsGenerica = new String();
	private SuperPost superPostGenerico = new SuperPost();
	private Messaggio messaggioNuovoPost = new Messaggio();
	private Post postGenerico = new Post();
	private Messaggio messaggioNuovoThread = new Messaggio();
	private Messaggio messaggioRispostaThread = new Messaggio();	
	private Messaggio messaggioRispostaPost = new Messaggio();
	private Messaggio messaggioRispostaThr = new Messaggio();
	
	
	//ListaSuperPost
	public void caricaListaSuperPost (int tipoLista) {
		int idIniz = 0;
		int idFinal = 0;
		titoloListaSuperPostsGenerica ="";
		switch (tipoLista) {
		case 1://FORUM UTENTI FREE
			idIniz = 1;
			idFinal = 20;
			titoloListaSuperPostsGenerica ="UTENTi FREe";
			break;
		case 2://FORUM UTENTI PLUS
			idIniz = 21;
			idFinal = 40;
			titoloListaSuperPostsGenerica ="UTENTi PLUs";
			break;
		case 3://FEEDBACK UTENTI FREE
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		case 4://FEEDBACK UTENTI PLUS
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		case 5://LAVORI - RICHIESTA & OFFERTA	
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		case 6://LAVORI - CASTING & AGENZIE
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		case 7://FOTOGRAFIA - ATTREZZATURE & TECNICHE
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		case 8://FOTOGRAFIA - CONSIGLI & TRUCCHI
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		case 9://MODA - ULTIMI TREND
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		case 10://MODA - STILISTI & SFILATE
			idIniz = 0;
			idFinal = 0;
			titoloListaSuperPostsGenerica ="";
			break;
		default:
			break;
		}
		String query = "from SuperPost p where p.id between "+idIniz+" and "+idFinal;
		EntityManager em = EMF.createEntityManager();
		List<SuperPost> superposts = em
		.createQuery(query)
		.getResultList();
		if(superposts!=null && superposts.size()>0){
			listaSuperPostsGenerica = superposts;
			
			for (SuperPost sp : listaSuperPostsGenerica) {		
//				System.out.println("inizio ordinamentoooo - SP id= "+sp.getId());
				for (Post p : sp.getPosts()) {
//					System.out.println("inizio ordinamentoooo - P id= "+p.getId());
					for (Thread t : p.getRisposte()) {
//						System.out.println("inizio ordinamentoooo - TD id= "+t.getId());
						ordinaMessaggiPerData(t.getMessaggi());
					}
					ordinaThreadPerData(p.getRisposte());
				}
				ordinaPostPerData(sp.getPosts());
			}
			System.out.println("dopo ordinamentoooo");
			contentBean.setContent("forumListaGenerica2.xhtml");
		}
	}

	
	
	//SuperPost=Lista Posts
	public void visualizzaSuperPost(SuperPost sp) {
		superPostGenerico = sp;
		for (Post p : superPostGenerico.getPosts()) {
//			System.out.println("inizio ordinamentoooo - P id= "+p.getId());
			for (Thread t : p.getRisposte()) {
//				System.out.println("inizio ordinamentoooo - TD id= "+t.getId());
				ordinaMessaggiPerData(t.getMessaggi());
			}
			ordinaThreadPerData(p.getRisposte());
		}
		ordinaPostPerData(superPostGenerico.getPosts());
		contentBean.setContent("forumSuperPostGenerico.xhtml");
		System.out.println("sp.posts(0).id=== "+superPostGenerico.getPosts().get(0).getId());
	}
	
	
	public void nuovoPost (SuperPost sp) {
		Post p = new Post();
		Thread t = new Thread();
		Messaggio m = new Messaggio();
		m.setMittente(utenteBean.getUtente());
		m.setDestinatario(utenteBean.getUtente());
		m.setOggetto(messaggioNuovoPost.getOggetto());
		m.setMessaggio(messaggioNuovoPost.getMessaggio());
		Date d = new Date();
		m.setData(d);
		m.setThread(t);
		t.getMessaggi().add(m);
		t.setOggettoThread(messaggioNuovoPost.getOggetto());
		t.setMittentePrimo(utenteBean.getUtente());
		t.setDestinatarioPrimo(utenteBean.getUtente());
		t.setPost(p);
		p.getRisposte().add(t);
		p.setProponente(utenteBean.getUtente());
		p.setSuperpost(sp);
		sp.getPosts().add(p);
		try {
			serv.merge(sp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		visualizzaSuperPost(sp);
	}
	
	
	public void visualizzaPost (Post p) {
		System.out.println("visualizzaPost START");
		for (Thread t : p.getRisposte()) {
			ordinaIversamenteMessaggiPerData(t.getMessaggi());
		}
		ordinaInversamenteThreadPerData(p.getRisposte());
		postGenerico = p;
		contentBean.setContent("forumPostGenerico2.xhtml");
	}
	

	public void inviaRispostaPost (Post p) {
		Messaggio mess = new Messaggio();
		Thread thr = new Thread();
		mess.setMittente(utenteBean.getUtente());
		mess.setDestinatario(p.getProponente());
		mess.setThread(thr);
		mess.setOggetto("Re: "+p.getRisposte().get(p.getRisposte().size()-1).getOggettoThread());
		mess.setMessaggio(messaggioRispostaPost.getMessaggio());
		Date ora = new Date();
		mess.setData(ora);
		thr.getMessaggi().add(mess);
		thr.setDestinatarioPrimo(p.getProponente());
		thr.setMittentePrimo(utenteBean.getUtente());
		thr.setOggettoThread("Re: "+p.getRisposte().get(p.getRisposte().size()-1).getOggettoThread());
		thr.setPost(p);
		p.getRisposte().add(thr);
		try {
			serv.merge(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messaggioRispostaPost = new Messaggio();
		contentBean.setMessaggio("risposta al post inviata!");
		visualizzaPost(p);
	}
	
	
	public void inviaRispostaThread (Thread t) {
		System.out.println("risposta thread inviata START");
		Messaggio mess = new Messaggio();
		mess.setMittente(utenteBean.getUtente());
		mess.setDestinatario(t.getMittentePrimo());
		mess.setThread(t);
		mess.setOggetto("Re: "+t.getMessaggi().get(t.getMessaggi().size()-1).getOggetto());
		mess.setMessaggio(messaggioRispostaThr.getMessaggio());
		Date ora = new Date();
		mess.setData(ora);
		t.getMessaggi().add(mess);
		try {
			serv.merge(t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentBean.setMessaggio("risposta secondaria inviata!");
		contentBean.setContent("forumPostGenerico2.xhtml");
	}
	
	private List<Thread> listThr;
	@PostConstruct
	public void init() {
		System.out.println("postConstruct"+postGenerico.getRisposte().size());
		listThr = new ArrayList<Thread>();
		listThr = postGenerico.getRisposte();
		}
	
	public void threadInRispostaSet(Thread thr) {

//		Thread thr = null;
//
//	    UIComponent tmpComponent = ev.getComponent();
//
//	    while (null != tmpComponent && !(tmpComponent instanceof UIData)) {
//	      tmpComponent = tmpComponent.getParent();
//	    }
//
//	    if (tmpComponent != null && (tmpComponent instanceof UIData)) {
//	      Object tmpRowData = ((UIData) tmpComponent).getRowData();
//	      if (tmpRowData instanceof Thread) {
//	    	  thr = (Thread) tmpRowData;
//
//	    //TODO Implementation of your method
//
//	      }
//	    }
//	    
//	    
////		Thread thr = null
		System.out.println("GETID: " + thr.getId());
		threadInRisposta = thr;
		messaggioRispostaThr.setMessaggio("CITANDo P|S: "+thr.getMessaggi().get(0).getMittente().getUsername()+" - MESSAGGIo: "+thr.getMessaggi().get(0).getMessaggio()+"\n");
	}
	
	
	public void threadInRispostaSet2(Messaggio mess) {
		messaggioRispostaThread = mess;
		messaggioRispostaThr.setMessaggio("CITANDo P|S: "+mess.getMittente().getUsername()+" - MESSAGGIo: "+mess.getMessaggio()+"\n");
	}	
	
	
	public int numeroMessaggiPost(Post p) {
		int numeroMessaggi = 0;
		for (Thread t : p.getRisposte()) {
			numeroMessaggi += t.getMessaggi().size();
		}
		return numeroMessaggi;
	}
	
	
	
	
	/////////////////sistemare x regolamento e affini...con ordinamento

	public void regolamentoForum() {
		EntityManager em = EMF.createEntityManager();
		List<Post> posts = em
		.createQuery("from Post p where p.id = 1")
		.getResultList();
		postUtente = posts.get(0);
		contentBean.setContent("forumRegolamento.xhtml");
	}
	
	
	public void inviaRispostaPostRegolamento (Post p) {
		Messaggio mess = new Messaggio();
		Thread thr = new Thread();
		mess.setMittente(utenteBean.getUtente());
		mess.setDestinatario(p.getProponente());
		mess.setThread(thr);
		mess.setOggetto("Re: "+p.getRisposte().get(p.getRisposte().size()-1).getOggettoThread());
		mess.setMessaggio(messaggioRispostaPost.getMessaggio());
		Date ora = new Date();
		mess.setData(ora);
		thr.getMessaggi().add(mess);
		thr.setDestinatarioPrimo(p.getProponente());
		thr.setMittentePrimo(utenteBean.getUtente());
		thr.setOggettoThread("Re: "+p.getRisposte().get(p.getRisposte().size()-1).getOggettoThread());
		thr.setPost(p);
		p.getRisposte().add(thr);
		try {
			serv.merge(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messaggioRispostaPost = new Messaggio();
		contentBean.setMessaggio("risposta al post inviata!");
		contentBean.setContent("forumRegolamento.xhtml");
	}
	
	
	public void inviaRispostaThreadRegolamento (Thread t) {
		System.out.println("risposta thread inviata START");
		Messaggio mess = new Messaggio();
		mess.setMittente(utenteBean.getUtente());
		mess.setDestinatario(t.getMittentePrimo());
		mess.setThread(t);
		mess.setOggetto("Re: "+t.getMessaggi().get(t.getMessaggi().size()-1).getOggetto());
		mess.setMessaggio(messaggioRispostaThr.getMessaggio());
		Date ora = new Date();
		mess.setData(ora);
		t.getMessaggi().add(mess);
		try {
			serv.merge(t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentBean.setMessaggio("risposta secondaria inviata!");
		contentBean.setContent("forumRegolamento.xhtml");
	}
//	
//	public void forumListaGenerica(int tipoLista) {
//		String tipo = "";
//		switch (tipoLista) {
//		case 1://FORUM UTENTI FREE
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 2://FORUM UTENTI PLUS
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 3://FEEDBACK UTENTI FREE
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 4://FEEDBACK UTENTI PLUS
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 5://LAVORI - RICHIESTA & OFFERTA	
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 6://LAVORI - CASTING & AGENZIE
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 7://FOTOGRAFIA - ATTREZZATURE & TECNICHE
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 8://FOTOGRAFIA - CONSIGLI & TRUCCHI
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 9://MODA - ULTIMI TREND
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		case 10://MODA - STILISTI & SFILATE
//			tipo = "p.postUtentiFree";
//			titoloListaPostsGenerica = "POSTs UTENTi FREe";
//			break;
//		default:
//			break;
//		}
//		String query = "from Post p where "+tipo+" = true";
//		EntityManager em = EMF.createEntityManager();
//		List<Post> posts = em
//		.createQuery(query)
//		.getResultList();
//		if(posts!=null && posts.size()>0){
//			listaPostsGenerica = posts;
//			
//			for (Post p : listaPostsGenerica) {
////				System.out.println("dopo ordinamentoooo messaggi");
//				for (Thread t : p.getRisposte()) {
//					ordinaMessaggiPerData(t.getMessaggi());
////					int i;
////					System.out.println("threadSize="+t.getMessaggi().size());
////					for (i=0;i<t.getMessaggi().size();i++) {
////						System.out.println("m("+i+")"+t.getMessaggi().get(i).getData());
////					}
//				}
//				ordinaThreadPerData(p.getRisposte());
////				System.out.println("p(0)"+p.getRisposte().get(0).getOggettoThread()+"data"+p.getRisposte().get(0).getMessaggi().get(0).getData());
////				System.out.println("p(1)"+p.getRisposte().get(1).getOggettoThread()+"data"+p.getRisposte().get(1).getMessaggi().get(0).getData());
////				System.out.println("p(2)"+p.getRisposte().get(2).getOggettoThread()+"data"+p.getRisposte().get(2).getMessaggi().get(0).getData());
//			}
//			
//			contentBean.setContent("forumListaGenerica.xhtml");
//		}
//	}
//	
//	
//	public void rispondiPost(Post p) {
//		Thread thr = new Thread();
//		thr.setOggettoThread(messaggioNuovoThread.getOggetto());
//		thr.setMittentePrimo(utenteBean.getUtente());
//		thr.setDestinatarioPrimo(postGenerico.getProponente());
//		Messaggio mess = new Messaggio();
//		mess.setMittente(utenteBean.getUtente());
//		mess.setDestinatario(p.getProponente());
//		mess.setOggetto(messaggioNuovoThread.getOggetto());
//		mess.setMessaggio(messaggioNuovoThread.getMessaggio());
//		Date ora = new Date();
//		mess.setData(ora);
//		mess.setThread(thr);
//		thr.getMessaggi().add(mess);
//		thr.setPost(p);
//		p.getRisposte().add(thr);
//		try {
//			serv.merge(p);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ordinaThreadPerData(postGenerico.getRisposte());
//		messaggioNuovoThread = new Messaggio();
//		contentBean.setMessaggio("risposta al post inviata!");
//		contentBean.setContent("forumPostGenerico.xhtml");
//	}
	
	
//	public void rispondiThread(Thread t) {
//		Messaggio mess = new Messaggio();
//		mess.setMittente(utenteBean.getUtente());
//		mess.setDestinatario(t.getMittentePrimo());
//		mess.setOggetto(messaggioThreadGenerico.getOggetto());
//		mess.setMessaggio(messaggioThreadGenerico.getMessaggio());
//		Date ora = new Date();
//		mess.setData(ora);
//		mess.setThread(t);
//		t.getMessaggi().add(mess);
//		try {
//			serv.merge(t);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ordinaMessaggiPerData(t.getMessaggi());
//		messaggioThreadGenerico = new Messaggio();
//		contentBean.setMessaggio("risposta al thread inviata!");
//		contentBean.setContent("forumThread.xhtml");
//	}
//	
	
//	public void messaggioInRispostaSet (Messaggio m) {
//		messaggioInRisposta = m;
//		messaggioInRisposta.setMessaggio("CITANDo P|S: "+m.getMittente().getUsername()+" - MESSAGGIo: "+m.getMessaggio()+"\n");
//	}
//	
//	
//	public void inviaMessaggioRispostaThread (Messaggio m) {
//		
//	}
	
	
	
	
	

	
	
	
	
	IndirectListSorter<Messaggio> messaggiSorter = new IndirectListSorter<Messaggio>();
	
	public void ordinaMessaggiPerData(List<Messaggio> listMess ) {	
		System.out.println("ORDINA MESSAGGI:#"+listMess.size());
		//ordina per ultimo invio/ricezione	
		messaggiSorter.sortIndirectList(listMess, new MessaggiComparator());
	}
	
	public void ordinaIversamenteMessaggiPerData(List<Messaggio> listMess ) {	
		System.out.println("ORDINA INVERSA MESSAGGI:#"+listMess.size());
		//ordina per ultimo invio/ricezione	
		messaggiSorter.sortIndirectList(listMess, new MessaggiComparator() {
			@Override
			public int compare(Messaggio u1, Messaggio u2) {
				int c = u1.getData().compareTo(u2.getData());
//				System.out.println("comparing u2 "+u2.getMessaggi().get(0).getData()+" and u1 "+u1.getMessaggi().get(0).getData()+" : "+c);
				return c;
			}
		});
	}
	
	
	IndirectListSorter<Thread> threadsSorter = new IndirectListSorter<Thread>();	
	
	public void ordinaThreadPerData(List<Thread> listThr ) {	 
		System.out.println("ORDINA THREADS:#"+listThr.size());
		//ordina per ultimo invio/ricezione
		threadsSorter.sortIndirectList(listThr, new Comparator<Thread>() {
			@Override
			public int compare(Thread u1, Thread u2) {
				int c = u2.getMessaggi().get(0).getData().compareTo(u1.getMessaggi().get(0).getData());
//				System.out.println("comparing u2 "+u2.getMessaggi().get(0).getData()+" and u1 "+u1.getMessaggi().get(0).getData()+" : "+c);
				return c;
			}
		});
	}	
	
	public void ordinaInversamenteThreadPerData(List<Thread> listThr ) {	 
		System.out.println("ORDINA INVERSA THREADS:#"+listThr.size());
		//ordina per ultimo invio/ricezione
		threadsSorter.sortIndirectList(listThr, new Comparator<Thread>() {
			@Override
			public int compare(Thread u1, Thread u2) {
				int c = u1.getMessaggi().get(0).getData().compareTo(u2.getMessaggi().get(0).getData());
//				System.out.println("comparing u2 "+u2.getMessaggi().get(0).getData()+" and u1 "+u1.getMessaggi().get(0).getData()+" : "+c);
				return c;
			}
		});
	}	
	
	
	IndirectListSorter<Post> postsSorter = new IndirectListSorter<Post>();
	
	public void ordinaPostPerData(List<Post> listPosts ) {	 
		System.out.println("ORDINA POSTS:#"+listPosts.size());
		//ordina per ultimo invio/ricezione
		postsSorter.sortIndirectList(listPosts, new PostsComparator());
	}
	
	
	
	
	

//	public boolean messaggioIsLetto (int idUtente, Messaggio mess) {
//		for (Utente uts : mess.getLetto()) {
//			if(uts.getId()==idUtente){
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	
//	public boolean threadContieneMessaggiNonLetti (int idUtente, Thread thread) {
//		int j=0;
//		for (Messaggio mess : thread.getMessaggi()) {
//			int i=0;
////			System.out.println("id mess: "+mess.getId());
//			for (Utente ut : mess.getLetto()) {
//				if (ut.getId()==idUtente) {
//					i++;
////					System.out.println("ut.getId()==idUtente__i="+i);
//				}
//			}
//			if (i==0) {
//				j++;
////				System.out.println("i==0__j="+j);
//			}
//		}
//		if (j>0) {
//			System.out.println("threadContieneMessaggiNonLetti=TRUE_oggetto: "+thread.getOggettoThread());
//			return true;
//		}
//		System.out.println("threadContieneMessaggiNonLetti=FALSE_oggetto: "+thread.getOggettoThread());
//		return false;
//	}
//	
//	

//	
//	
//	IndirectListSorter<Annuncio> annunciSorter = new IndirectListSorter<Annuncio>();	
//	
//	public void ordinaAnnunciPerData(List<Annuncio> listAnn ) {	 
//		System.out.println("ORDINA Annunci:#"+listAnn.size());
//		//ordina per ultima pubblicazione
//		annunciSorter.sortIndirectList(listAnn, new AnnunciComparator());
//	}
//	
//
//	public void pubblicaNuovoAnnuncio () {
//		System.out.println("PUBBLICA NUOVO ANNUNCIO function");
//		Annuncio ann = new Annuncio();
//		Thread thr = new Thread();	
//		Messaggio mess = new Messaggio();
//		thr.setMittentePrimo(utenteBean.getUtente());
//		thr.setDestinatarioPrimo(utenteBean.getUtente());
//		thr.setOggettoThread(messaggioNuovoAnnuncio.getOggetto());
//		mess.setMessaggio(messaggioNuovoAnnuncio.getMessaggio());
//		mess.setMittente(utenteBean.getUtente());
//		mess.setDestinatario(utenteBean.getUtente());
//		mess.setOggetto(messaggioNuovoAnnuncio.getOggetto());
//		mess.setThread(thr);
//		mess.getLetto().add(utenteBean.getUtente());
//		Date ora = new Date();
//		mess.setData(ora);
//		thr.getMessaggi().add(mess);
//		thr.setAnnuncio(ann);
//		thr.setNuovoMessaggio(false);
//		ann.setCitt�Annuncio(nuovoAnnuncio.getCitt�Annuncio());
//		ann.setProponente(utenteBean.getUtente());
//		ann.getRisposte().add(thr);
//		try {
//			serv.persist(ann);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace(); 
//		}
//		nuovoAnnuncio.setCitt�Annuncio(null);
//		setMessaggioNuovoAnnuncio(null);
//		messaggioNuovoAnnuncio = new Messaggio();
//		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
//		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("headerform");
//		contentBean.setContent("annunci.xhtml");
//		contentBean.setMessaggio("annuncio nuovo");
//	}
//
//	
//	public void ripubblicaAnnuncio(Annuncio ann) {
//		Date ora = new Date();
//		ann.getRisposte().get(0).getMessaggi().get(ann.getRisposte().get(0).getMessaggi().size()-1).setData(ora);
//		try {
//			serv.merge(ann);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	
//	public void cercaAnnunciRispostiDaUtente (int idUtente) {
//		EntityManager em = EMF.createEntityManager();
//		annunciRispostiDaUtente = em
//		.createQuery("from Annuncio a inner join a.risposte ar where a.proponente.id <>:prop and ar.mittentePrimo.id =:prop and ar.cancellatoThreadMittente = false")
//		.setParameter("prop", idUtente)
//		.getResultList();
//		ordinaAnnunciPerData(annunciRispostiDaUtente);
//		System.out.println("annunciRispostiDaUtente size="+annunciRispostiDaUtente.size());
//	}
//	
//	
//	public void cercaAnnunciPubblicatiDaUtente (int idUtente) {
//		EntityManager em = EMF.createEntityManager();
//		annunciUtente = em
//		.createQuery("from Annuncio a where a.proponente.id =:prop")
//		.setParameter("prop", idUtente)
//		.getResultList();
//		ordinaAnnunciPerData(annunciUtente);	
//		System.out.println("annunciUtente size="+annunciUtente.size());
//	}
//	
//	
//	private Map<Integer, Boolean> idAnnunciSelezionati = new HashMap<Integer, Boolean>();	
//	
//	public void cancellaAnnunciPubblicatiSelezionati () {
//		System.out.println("Cancella annunci pubblicati START");
//		for (Annuncio a : annunciUtente)  {
//			if (idAnnunciSelezionati.get(a.getId()).booleanValue()) {
//				System.out.println("delete annuncio ID="+a.getId());
//				try {
//					serv.delete(a);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
//		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
//		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
//	}
//
//	
//	public void cancellaThreadsAnnunciRispostiSelezionati () {
//		System.out.println("Cancella annunci START");
//		for (Annuncio a : annunciRispostiDaUtente)  {
//			if (idAnnunciSelezionati.get(a.getId()).booleanValue()) {
//				System.out.println("delete thread risposta annuncio ID="+a.getId());
//				for (Thread thread : a.getRisposte()) {
//					if(thread.getMittentePrimo().getId()==utenteBean.getUtente().getId()) {
//						System.out.println("thr da cancellare id="+thread.getId());
//						try {
//							thread.setCancellatoThreadMittente(true);
////							thread.setNuovoMessaggio(false);
//							if (thread.isCancellatoThreadDestinatario()) {
//								serv.delete(thread);
//								System.out.println("delete");
//							}
//							else {
//								serv.merge(thread);	
//								System.out.println("merge");
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}	
//					}
//				}
//			}
//		}
//		nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
//		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
//		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
//	}
//	
//	
//	public boolean annuncioContieneRisposteNonLette (int idUtente, Annuncio ann) {
//		for (Thread thr : ann.getRisposte()) {
//			int j=0;
//	//		System.out.println("annuncioContieneRisposteNonLette size="+ann.getRisposte().get(0).getMessaggi().size());
//			for (Messaggio mess : thr.getMessaggi()) {
////				escludo il primo messaggio che � quello dell'annuncio, dove mittente=destinatario
//				if(mess.getMittente().getId()!=mess.getDestinatario().getId()) {
//					int i=0;
//					System.out.println("id mess: "+mess.getId());
//					for (Utente ut : mess.getLetto()) {
//						if (ut.getId()==idUtente) {
//							i++;
//							System.out.println("ut.getId()==idUtente__i="+i);
//						}
//					}
//		//			i=0 -> ci sono mess non letti
//					if (i==0) {
//						j++;
//						System.out.println("mess non letto ID="+mess.getId());
//					}
//				}
//			}
//			if (j>0) {
//				System.out.println("annuncioContieneRisposteNonLette=TRUE_oggetto: "+ann.getRisposte().get(0).getOggettoThread());
//				return true;
//			}
//		}
//		System.out.println("annuncioContieneRisposteNonLette=FALSE_oggetto: "+ann.getRisposte().get(0).getOggettoThread());
//		return false;
//	}
//	
//	
//	public boolean annuncioContieneThreadNuovo (Annuncio ann) {
//			for (Thread thr : ann.getRisposte()) {
//				if(thr.isNuovoMessaggio())
//					return true;
//			}
//		return false;
//	}
//	
//	
//	public void visualizzaAnnuncioPubblicato (Annuncio ann) {
//		setAnnuncioPubblicato(ann);
//		setThreadAnnuncioPubblicato(null);
//		System.out.println("annunciopubblicato id="+ann.getId());
//		contentBean.setMessaggio(null);
//		contentBean.setContent("annuncioPubblicato.xhtml");		
//	}
//	
//	
//	public void visualizzaAnnuncioAltrui (Annuncio ann) {
//		System.out.println("visualizzaAnnuncioAltrui STARTTT  proponente=="+ann.getProponente().getUsername());
//		annuncioAltrui = ann;
////		ordinaThreadPerData(ann.getRisposte());
//		esisteRispostaAnnuncio(utenteBean.getUtente(), ann);
//		System.out.println("threadRispostaAnnuncio starttt ID="+threadRispostaAnnuncio.getId());
//		boolean nuovoThread = false;
//		boolean nuovoMess = false;
//		if (!threadRispostaAnnuncio.isNuovoMessaggio()) {
//			System.out.println("thread non nuovo");
//			for (Messaggio m : threadRispostaAnnuncio.getMessaggi()) {
////					se l'utente (destinatario) non ha letto il messaggio (x non duplicare primary key)
//				if (!messaggioIsLetto(m.getDestinatario().getId(), m))
//				{
//					System.out.println("messaggio annuncioAltrui non letto da destinatario (me)!");
//					m.getLetto().add(m.getDestinatario());
//					nuovoThread = true;
//				}		
//			}
//		}				
////			controllo se il thread che leggo � contenuto nella lista dei nuovi thread
//		System.out.println("threadsAnnunciConNuoviMessaggi size= "+threadsAnnunciConNuoviMessaggi.size());
//		for (Thread t : threadsAnnunciConNuoviMessaggi) {
//			System.out.println("threadsAnnunciConNuoviMessaggi ID="+t.getId());
//			System.out.println("threadRispostaAnnuncio ID="+threadRispostaAnnuncio.getId());
//			if (t.getId()==threadRispostaAnnuncio.getId())	{
//				System.out.println("thread annuncioAltrui con nuovo mess contenuto in threadsAnnunciConNuoviMessaggi");
//				threadRispostaAnnuncio.setNuovoMessaggio(false);
//				nuovoMess = true;
//			}				
//		}
//		if (nuovoMess || nuovoThread) {
//			try {
//				serv.merge(threadRispostaAnnuncio);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//		}
//		ordinaThreadPerData(ann.getRisposte());
//		ordinaMessaggiPerData(threadRispostaAnnuncio.getMessaggi());
//		if (utenteBean.getUtente()!=null) {
//				nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
//		}
//		contentBean.setMessaggio(null);
//		contentBean.setContent("annuncioAltrui.xhtml");		
//	}
//	
//	
//	public void visualizzaThreadAnnuncioPubblicato (Thread thread) {
//		System.out.println("visualizzaThreadAnnuncioPubblicato START");
//		threadAnnuncioPubblicato = thread;
//		boolean nuovoThread = false;
//		boolean nuovoMess = false;
////		se non � nuovo thread 			
//		System.out.println("threadAnnuncioPubblicato nuovo????"+threadAnnuncioPubblicato.isNuovoMessaggio());
//		if (!threadAnnuncioPubblicato.isNuovoMessaggio()) {
//			System.out.println("thread non ha nuovimessaggi");
//			for (Messaggio m : threadAnnuncioPubblicato.getMessaggi()) {
////					se l'utente (destinatario) non ha letto il messaggio (x non duplicare primary key)
//				if (!messaggioIsLetto(m.getDestinatario().getId(), m))
//				{
//					System.out.println("messaggio non letto da destinatario (me)!");
//					m.getLetto().add(m.getDestinatario());
//					nuovoThread = true;
//				}		
//			}
//		}				
////			controllo se il thread che leggo � contenuto nella lista dei nuovi thread
//		System.out.println("threadsAnnunciConNuoviMessaggi size= "+threadsAnnunciConNuoviMessaggi.size());
//		for (Thread t : threadsAnnunciConNuoviMessaggi) {;
//			if (t.getId()==threadAnnuncioPubblicato.getId())	{
//				System.out.println("thread annuncio con nuovo mess contenuto in threadsAnnunciConNuoviMessaggi");
//				threadAnnuncioPubblicato.setNuovoMessaggio(false);
//				nuovoMess = true;
//			}				
//		}	
//		if (nuovoMess || nuovoThread) {
//			try {
//				serv.merge(threadAnnuncioPubblicato);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		ordinaMessaggiPerData(threadAnnuncioPubblicato.getMessaggi());			
//		nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
//		contentBean.setContent("annuncioThread.xhtml");		
//	}
//	
//	
//	public void nuoviMessaggiThreadsAnnuncio (int idUtente) {
//		threadsAnnunciConNuoviMessaggi.clear();
//		System.out.println("nuoviMESSAGGIANNUNCIIIIIingresso");
//		
//		List<Annuncio> annunciConNuoviMessaggiInThreadsToT = new ArrayList<Annuncio>();
//		List<Annuncio> annunciTotUtente = new ArrayList<Annuncio>();
//		annunciTotUtente.addAll(annunciUtente);
//		annunciTotUtente.addAll(annunciRispostiDaUtente);
//		if(annunciUtente.size()>0){
//			System.out.println("annunciUtente.risposte() size="+annunciTotUtente.get(0).getRisposte().size());
//		}
//
//		if (annunciRispostiDaUtente.size()>0) {
//			System.out.println("annunciRispostiDaUtente.risposte() size="+annunciRispostiDaUtente.get(0).getRisposte().size());
//		}
//		System.out.println("annunciTotUtente size="+annunciTotUtente.size());
//		for (Annuncio annuncio : annunciTotUtente) {
//			System.out.println("annunciTotUtente id="+annuncio.getId());
//			System.out.println("annunciTotUtente thread.size===="+annuncio.getRisposte().size());
//			for (Thread thr : annuncio.getRisposte()) {
//				System.out.println("thread annuncio id="+thr.getId());
//				if (thr.isNuovoMessaggio()) {
//					System.out.println("isNuovoMessaggio== TRUEEE id="+thr.getId());
//					annunciConNuoviMessaggiInThreadsToT.add(annuncio);
//				}
//			}
//		}
//		System.out.println("****ANNUNCIO Pubblicati+Risposti #"+annunciConNuoviMessaggiInThreadsToT.size());
//
//		int numthr = 0;		
//		for (Annuncio ann : annunciConNuoviMessaggiInThreadsToT) {	
//			for (Thread thr : ann.getRisposte()) {
//				System.out.println("THREAD ID="+thr.getId());
//	//			controllo se non ho cancellato il thread - in caso lo avessi cancellato, non lo conteggio anche se contiene mess nuovi x me
//				if ((thr.getMittentePrimo().getId()==utenteBean.getUtente().getId() && thr.isCancellatoThreadMittente()==false) || (thr.getDestinatarioPrimo().getId()==utenteBean.getUtente().getId() && thr.isCancellatoThreadDestinatario()==false)) {	
//					if (threadContieneMessaggiNonLetti(idUtente, thr)) {
//						System.out.println("TRUE=contiene"+threadContieneMessaggiNonLetti(idUtente, thr));
//						numthr++;
//						threadsAnnunciConNuoviMessaggi.add(thr);
//					}
//				}
//			}
//		}
//		System.out.println("threads annunci con messaggi non letti size"+threadsAnnunciConNuoviMessaggi.size());
//		nuoviMessaggiAnnunci = numthr;
//		System.out.println("nuoviMessaggiAnnunci==="+nuoviMessaggiAnnunci);
//	}
//	
//	
//	public void aggiungiRispostaThreadAnnuncio() {
//		System.out.println("AGGIUNGI RISPOSTA ANNUNCIO function");
//		Messaggio mess = new Messaggio();
//		mess.setOggetto(threadAnnuncioPubblicato.getOggettoThread());
//		mess.setMessaggio(messaggioAggiunto.getMessaggio());
//		mess.setMittente(utenteBean.getUtente());
//		mess.setDestinatario(threadAnnuncioPubblicato.getMittentePrimo());
//		mess.setThread(threadAnnuncioPubblicato);
//		Date ora = new Date();
//		mess.setData(ora);
//		mess.getLetto().add(utenteBean.getUtente());
//		threadAnnuncioPubblicato.getMessaggi().add(mess);
//		if(!threadAnnuncioPubblicato.isNuovoMessaggio()){
//			for (Messaggio m : threadAnnuncioPubblicato.getMessaggi()) {
////				System.out.println("MESSAGGIO id="+m.getId()+" - UTENTE (Destinatario) id="+m.getDestinatario().getId());
//				if (!messaggioIsLetto(utenteBean.getUtente().getId(), m)) {
////					System.out.println("AGGIUNGO MESSAGGIO - UTENTE (Destinatario) id="+m.getDestinatario().getId()+" in m.letto messID="+m.getId());
//					m.getLetto().add(utenteBean.getUtente());
//				}
//			}
//		}
//		threadAnnuncioPubblicato.setNuovoMessaggio(true);
//		try {
//			serv.merge(threadAnnuncioPubblicato);
//			if(threadAnnuncioPubblicato.getMittentePrimo().isMailNuovaRispostaAnnuncio()) {
//				MailSender.sendNuovaRispostaAnnuncioAltruiMail(threadAnnuncioPubblicato.getMittentePrimo().getEmail(), utenteBean.getUtente().getUsername());
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace(); 
//		}
//		messaggioAggiunto.setMessaggio(null);
//		mess = null;
//		setThreadAnnuncioPubblicato(null);
//		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
//		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
//		contentBean.setContent("annunci.xhtml");
//		contentBean.setMessaggio("risposta annuncio aggiunta");
//	}
//	
//	
//	public void rispondiAnnuncio(Annuncio a) {
//		System.out.println("RISPONDI ANNUNCIO function");
////		se � la mia prima risposta all'annuncio - creo nuovo thread
//		if(!esisteRispostaAnnuncio(utenteBean.getUtente(), a)) {
//			Thread thr = new Thread();	
//			Messaggio mess = new Messaggio();
//			thr.setMittentePrimo(utenteBean.getUtente());
//			thr.setDestinatarioPrimo(a.getProponente());
//			thr.setOggettoThread(a.getRisposte().get(0).getOggettoThread());
//			mess.setMessaggio(messaggioRispostaAnnuncio.getMessaggio());
//			mess.setMittente(utenteBean.getUtente());
//			mess.setDestinatario(a.getProponente());
//			mess.setOggetto(a.getRisposte().get(0).getOggettoThread());
//			mess.setThread(thr);
//			mess.getLetto().add(utenteBean.getUtente());
//			Date ora = new Date();
//			mess.setData(ora);
//			thr.getMessaggi().add(mess);
//			thr.setAnnuncio(a);
//			thr.setNuovoMessaggio(true);
//			a.getRisposte().add(thr);
//			try {
//				serv.merge(a);
//				if(a.getProponente().isMailNuovaRispostaAnnuncio()) {
//					MailSender.sendNuovaRispostaAnnuncioMail(a.getProponente().getEmail(), utenteBean.getUtente().getUsername());
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace(); 
//			}
//		} else {
////			gi� risposto all'annuncio, uso lo stesso thread
//			System.out.println("gi� risposto all'annuncio, uso lo stesso thread");
//			Messaggio mess = new Messaggio();
//			mess.setMessaggio(messaggioRispostaAnnuncio.getMessaggio());
//			mess.setMittente(utenteBean.getUtente());
//			mess.setDestinatario(a.getProponente());
//			mess.setOggetto(a.getRisposte().get(0).getOggettoThread());
//			mess.setThread(threadRispostaAnnuncio);
//			mess.getLetto().add(utenteBean.getUtente());
//			Date ora = new Date();
//			mess.setData(ora);
//			if(!threadRispostaAnnuncio.isNuovoMessaggio()){
//				for (Messaggio m : threadRispostaAnnuncio.getMessaggi()) {
////					System.out.println("MESSAGGIO id="+m.getId()+" - UTENTE (Destinatario) id="+m.getDestinatario().getId());
//					if (!messaggioIsLetto(m.getDestinatario().getId(), m)) {
////						System.out.println("AGGIUNGO MESSAGGIO - UTENTE (Destinatario) id="+m.getDestinatario().getId()+" in m.letto messID="+m.getId());
//						m.getLetto().add(m.getDestinatario());
//					}
//				}
//			}
//			threadRispostaAnnuncio.getMessaggi().add(mess);
//			threadRispostaAnnuncio.setNuovoMessaggio(true);
////			a.getRisposte().add(threadRispostaAnnuncio);
//			try {
//				serv.merge(a);
//				if(a.getProponente().isMailNuovaRispostaAnnuncio()) {
//					MailSender.sendNuovaRispostaAnnuncioMail(a.getProponente().getEmail(), utenteBean.getUtente().getUsername());
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace(); 
//			}
//		}
//		messaggioRispostaAnnuncio.setMessaggio(null);
//		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
//		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
//		contentBean.setContent("annunci.xhtml");
//		contentBean.setMessaggio("risposto ad annuncio");
//	}
//	
//	
//	public boolean esisteRispostaAnnuncio (Utente ut, Annuncio a) {
//		System.out.println("esisteRispostaAnnuncio START");
//		if (ut!=null){
//			for (Thread thr : a.getRisposte()) {
//				System.out.println("a.risposte id===="+thr.getId());
//				System.out.println("a.risposte(0) id===="+a.getRisposte().get(a.getRisposte().size()-1).getId());
////				if (thr.getMittentePrimo().getId()==ut.getId() && (thr.getId()!=a.getRisposte().get(a.getRisposte().size()-1).getId())) {
//				if (thr.getMittentePrimo().getId()==ut.getId() && (thr.getMittentePrimo().getId()!=thr.getDestinatarioPrimo().getId())) {
//					setThreadRispostaAnnuncio(thr);
//					System.out.println("esisteRispostaAnnuncio =TRUE");
//					System.out.println("ThreadRispostaAnnuncio id=="+threadRispostaAnnuncio.getId());
//					System.out.println("thr id=="+thr.getId());
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//	
//	
//	public void cercaAnnunciSito() {
//		EntityManager em = EMF.createEntityManager();
//		String hqlstart = "from Annuncio a ";
//		String hqlcerca = "";
//		String hql = "";
//		int i = 0;
//		Boolean utenteLoggato = false;
//		if(utenteBean.tipoMembershipUtente()!=0){
//			hqlcerca += " a.proponente.id<>:idUt ";
//			utenteLoggato = true;
//			i++;
//		}
//		if (i>0){
//			hql = hqlstart + "where" + hqlcerca; 
//		}
//		else {
//			hql = hqlstart; 
//		}
//		Query q = em.createQuery(hql, Annuncio.class);
//		if(utenteLoggato){
//			q.setParameter("idUt",utenteBean.getUtente().getId());
//		}
//		System.out.println("ANNUNCI SITO hql="+hql);
//		annunciSito = q.getResultList();
//	}
//	
	
	
	
	
	
	
	
	//************GETTERS & SETTERS*******************
	

	public Post getPostUtente() {
		return postUtente;
	}

	public void setPostUtente(Post postUtente) {
		this.postUtente = postUtente;
	}

	public List<Thread> getThreadsRispostePost() {
		return threadsRispostePost;
	}

	public void setThreadsRispostePost(List<Thread> threadsRispostePost) {
		this.threadsRispostePost = threadsRispostePost;
	}

	public Messaggio getMessaggioRispostaPost() {
		return messaggioRispostaPost;
	}

	public void setMessaggioRispostaPost(Messaggio messaggioRispostaPost) {
		this.messaggioRispostaPost = messaggioRispostaPost;
	}
	
	public Messaggio getMessaggioRispostaThread() {
		return messaggioRispostaThread;
	}

	public void setMessaggioRispostaThread(Messaggio messaggioRispostaThread) {
		this.messaggioRispostaThread = messaggioRispostaThread;
	}

	public Thread getThreadInRisposta() {
		return threadInRisposta;
	}

	public void setThreadInRisposta(Thread threadInRisposta) {
		this.threadInRisposta = threadInRisposta;
	}

	public Messaggio getMessaggioRispostaThr() {
		return messaggioRispostaThr;
	}

	public void setMessaggioRispostaThr(Messaggio messaggioRispostaThr) {
		this.messaggioRispostaThr = messaggioRispostaThr;
	}	
	
	public List<Post> getListaPostsGenerica() {
		return listaPostsGenerica;
	}

	public void setListaPostsGenerica(List<Post> listaPostsGenerica) {
		this.listaPostsGenerica = listaPostsGenerica;
	}

	public String getTitoloListaPostsGenerica() {
		return titoloListaPostsGenerica;
	}

	public void setTitoloListaPostsGenerica(String titoloListaPostsGenerica) {
		this.titoloListaPostsGenerica = titoloListaPostsGenerica;
	}

	public Post getPostGenerico() {
		return postGenerico;
	}

	public void setPostGenerico(Post postGenerico) {
		this.postGenerico = postGenerico;
	}
	
	public Messaggio getMessaggioNuovoThread() {
		return messaggioNuovoThread;
	}

	public void setMessaggioNuovoThread(Messaggio messaggioNuovoThread) {
		this.messaggioNuovoThread = messaggioNuovoThread;
	}

	public Thread getThreadGenerico() {
		return threadGenerico;
	}

	public void setThreadGenerico(Thread threadGenerico) {
		this.threadGenerico = threadGenerico;
	}

	public List<SuperPost> getListaSuperPostsGenerica() {
		return listaSuperPostsGenerica;
	}

	public void setListaSuperPostsGenerica(List<SuperPost> listaSuperPostsGenerica) {
		this.listaSuperPostsGenerica = listaSuperPostsGenerica;
	}

	public String getTitoloListaSuperPostsGenerica() {
		return titoloListaSuperPostsGenerica;
	}

	public void setTitoloListaSuperPostsGenerica(
			String titoloListaSuperPostsGenerica) {
		this.titoloListaSuperPostsGenerica = titoloListaSuperPostsGenerica;
	}

	public SuperPost getSuperPostGenerico() {
		return superPostGenerico;
	}

	public void setSuperPostGenerico(SuperPost superPostGenerico) {
		this.superPostGenerico = superPostGenerico;
	}

	public Messaggio getMessaggioNuovoPost() {
		return messaggioNuovoPost;
	}

	public void setMessaggioNuovoPost(Messaggio messaggioNuovoPost) {
		this.messaggioNuovoPost = messaggioNuovoPost;
	}




	



	public List<Thread> getListThr() {
		return listThr;
	}

	public void setListThr(List<Thread> listThr) {
		this.listThr = listThr;
	}

	
	
	
	@ManagedProperty(value = "#{utenteBean}")
	private UtenteBean utenteBean;	


	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;
	
	
	public UtenteBean getUtenteBean() {
		return utenteBean;
	}

	public void setUtenteBean(UtenteBean utenteBean) {
		this.utenteBean = utenteBean;
	}

	public ContentBean getContentBean() {
		return contentBean;
	}

	public void setContentBean(ContentBean contentBean) {
		this.contentBean = contentBean;
	}


	
}