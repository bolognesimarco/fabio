package com.bolo.photoshooters.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.primefaces.context.RequestContext;

import com.bolo.photo.web.entity.Annuncio;
import com.bolo.photo.web.entity.Messaggio;
import com.bolo.photo.web.entity.Thread;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.util.AnnunciComparator;
import com.bolo.photoshooters.util.IndirectListSorter;
import com.bolo.photoshooters.util.MessaggiComparator;


@ManagedBean
@SessionScoped
public class ThreadBean {

	private ServiziComuni serv = new ServiziComuniImpl();
    List<Thread> threadsInviatiUtente = new ArrayList<Thread>();
    List<Thread> threadsRicevutiUtente = new ArrayList<Thread>();
    Thread threadMessaggi = new Thread();
    List<Thread> threadsConNuoviMessaggi = new ArrayList<Thread>();
	private Thread thread = new Thread();
	private Messaggio messaggio = new Messaggio();
	private Messaggio messaggioAggiunto = new Messaggio();
	private Thread threadEsistente = new Thread();
	private int nuoviMessaggi = 0;

	
	
	public void inviaNuovoMessaggio () {
		System.out.println("INVIA NUOVO MESSAGGIO function");
		if(messaggio.getDestinatario().getId()==utenteBean.getUtente().getId()){
			contentBean.setMessaggio("Destinatario non valido!");
		}
		else {
			if (!esisteThreadMessaggi(utenteBean.getUtente().getId(), messaggio.getDestinatario().getId(), messaggio.getOggetto())){
				System.out.println("INVIA MESSAGGIO==nuovo threadddddd");
				Thread thr = new Thread();	
				Messaggio mess = new Messaggio();
				thr.setMittentePrimo(utenteBean.getUtente());
				thr.setDestinatarioPrimo(messaggio.getDestinatario());
				thr.setOggettoThread(messaggio.getOggetto());
				mess.setMessaggio(messaggio.getMessaggio());
				mess.setMittente(utenteBean.getUtente());
				mess.setDestinatario(messaggio.getDestinatario());
				mess.setOggetto(messaggio.getOggetto());
				mess.setThread(thr);
				Date ora = new Date();
				mess.setData(ora);
				mess.getLetto().add(utenteBean.getUtente());
				thr.getMessaggi().add(mess);
				thr.setNuovoMessaggio(true);
				try {
					serv.persist(thr);
					if(mess.getDestinatario().isMailNuovoMessaggio()) {
					MailSender.sendNuovoMessaggioMail(mess.getDestinatario().getEmail(), mess.getMittente().getUsername());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace(); 
				}
				cercaThreadsInviatiUtente(utenteBean.getUtente().getId());
				cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
				int i=0;
				for (Messaggio m : thr.getMessaggi()) {
					System.out.println("InviaMessaggio-ordine MESS: #"+i+"-data-"+m.getData());
					i++;
				}	
				contentBean.setContent("messaggi.xhtml");
				contentBean.setMessaggio("Thread nuovo");
			} else {
				System.out.println("INVIA MESSAGGIO==thread esistente");
				if (threadEsistente.getMittentePrimo().getId()==utenteBean.getUtente().getId() && threadEsistente.isCancellatoThreadMittente()) {
					getContentBean().setMessaggio("Thread spedito già esistente: messaggio aggiunto!");
					threadEsistente.setCancellatoThreadMittente(false);
				} else {
					if (threadEsistente.isCancellatoThreadDestinatario()) {
						getContentBean().setMessaggio("Thread ricevuto già esistente: messaggio aggiunto!");
						threadEsistente.setCancellatoThreadDestinatario(false);
					}
				}
				Messaggio mess = new Messaggio();
				mess.setMessaggio(messaggio.getMessaggio());
				mess.setMittente(utenteBean.getUtente());
				mess.setDestinatario(messaggio.getDestinatario());
				mess.setOggetto(messaggio.getOggetto());
				mess.setThread(threadEsistente);
				Date ora = new Date();
				mess.setData(ora);
				mess.getLetto().add(utenteBean.getUtente());
				threadEsistente.getMessaggi().add(mess);
				threadEsistente.setNuovoMessaggio(true);
				try {
					serv.merge(threadEsistente);;
					if(mess.getDestinatario().isMailNuovoMessaggio()) {
						MailSender.sendNuovoMessaggioMail(mess.getDestinatario().getEmail(), mess.getMittente().getUsername());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cercaThreadsInviatiUtente(utenteBean.getUtente().getId());
				cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
				int i=0;
				for (Messaggio m : threadEsistente.getMessaggi()) {
					System.out.println("InviaMessaggio2-ordine MESS: #"+i+"-data-"+m.getData());
					i++;
				}
				contentBean.setContent("messaggi.xhtml");
			}
		}
	}
	
	
	public void aggiungiMessaggio () {
		System.out.println("AGGIUNGI MESSAGGIO function");
		Messaggio mess = new Messaggio();
		mess.setMessaggio(messaggioAggiunto.getMessaggio());
		mess.setMittente(utenteBean.getUtente());
//		se l'utente ha creato il thread
		if (threadMessaggi.getMittentePrimo().getId()==utenteBean.getUtente().getId()) {
			mess.setDestinatario(threadMessaggi.getDestinatarioPrimo());
		} else {
			mess.setDestinatario(threadMessaggi.getMittentePrimo());
		}
		mess.setOggetto(threadMessaggi.getOggettoThread());
		mess.setThread(threadMessaggi);
		Date ora = new Date();
		mess.setData(ora);
		mess.getLetto().add(utenteBean.getUtente());
//		System.out.println("thread isNuovoMessaggio="+threadMessaggi.isNuovoMessaggio());
		if(!threadMessaggi.isNuovoMessaggio()){
			for (Messaggio m : threadMessaggi.getMessaggi()) {
//				System.out.println("MESSAGGIO id="+m.getId()+" - UTENTE (Destinatario) id="+m.getDestinatario().getId());
				if (!messaggioIsLetto(m.getDestinatario().getId(), m)) {
//					System.out.println("AGGIUNGO MESSAGGIO - UTENTE (Destinatario) id="+m.getDestinatario().getId()+" in m.letto messID="+m.getId());
					m.getLetto().add(m.getDestinatario());
				}
			}
		}
		threadMessaggi.getMessaggi().add(mess);
		threadMessaggi.setNuovoMessaggio(true);
		try {
			serv.merge(threadMessaggi);
			if(mess.getDestinatario().isMailNuovoMessaggio()) {
				MailSender.sendNuovoMessaggioMail(mess.getDestinatario().getEmail(), mess.getMittente().getUsername());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("aGGIUNGI MeSSAGGIO threadMessaggi.getMessaggi().size()="+threadMessaggi.getMessaggi().size());
		messaggioAggiunto.setMessaggio(null);
		nuoviMessaggiThread(utenteBean.getUtente().getId());
		cercaThreadsInviatiUtente(utenteBean.getUtente().getId());
		cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
		contentBean.setContent("messaggi.xhtml");
	}
	
	
	public boolean esisteThreadMessaggi (int mittId, int destId, String oggThr) {
		System.out.println("ESISTETHREADH function");
		EntityManager em = EMF.createEntityManager();
		List<Thread> threads = em
		.createQuery("from Thread t where (t.mittentePrimo.id =:mitt or t.mittentePrimo.id =:dest) and (t.destinatarioPrimo.id =:dest or t.destinatarioPrimo.id =:mitt) and t.oggettoThread =:ogg")
		.setParameter("mitt", mittId)
		.setParameter("dest", destId)
		.setParameter("ogg", oggThr)
		.getResultList();		
		if(threads!=null && threads.size()>0) {
			setThreadEsistente(threads.get(0));
			return true;		
		}
		return false;
	}
	
	
	public void cercaThreadsInviatiUtente (int idUtente) {
		EntityManager em = EMF.createEntityManager();
		threadsInviatiUtente = em
		.createQuery("from Thread t where t.mittentePrimo.id =:mitt and t.cancellatoThreadMittente = false and t.annuncio is null")
		.setParameter("mitt", idUtente)
		.getResultList();
		for (Thread t : threadsRicevutiUtente) {
			System.out.println("threadINVIATI-NOordinati-oggetto-"+t.getOggettoThread()+"-data-"+t.getMessaggi().get(t.getMessaggi().size()-1).getData());
		}
		ordinaThreadPerData(threadsInviatiUtente);
		for (Thread t : threadsInviatiUtente) {
			System.out.println("threadsINVIATI-oggetto-"+t.getOggettoThread()+"-data-"+t.getMessaggi().get(t.getMessaggi().size()-1).getData());
		}		
	}
	
	
	public void cercaThreadsRicevutiUtente (int idUtente) {
		EntityManager em = EMF.createEntityManager();
		threadsRicevutiUtente = em
		.createQuery("from Thread t where t.destinatarioPrimo.id =:mitt and t.cancellatoThreadDestinatario = false and t.annuncio is null")
		.setParameter("mitt", idUtente)
		.getResultList();	
		for (Thread t : threadsRicevutiUtente) {
			System.out.println("threadRICEVUTI-NOordinati-oggetto-"+t.getOggettoThread()+"-data-"+t.getMessaggi().get(t.getMessaggi().size()-1).getData());
		}
		ordinaThreadPerData(threadsRicevutiUtente);
		for (Thread t : threadsRicevutiUtente) {
			System.out.println("threadRICEVUTI-ORDINATI-oggetto-"+t.getOggettoThread()+"-data-"+t.getMessaggi().get(t.getMessaggi().size()-1).getData());
		}
	}
	
	
//	public void visualizzaThread (int idThread) {
//		EntityManager em = EMF.createEntityManager();
//		List<Thread> threads = em
//		.createQuery("from Thread t where t.id =:idThr")
//		.setParameter("idThr", idThread)
//		.getResultList();	
//		if(threads!=null && threads.size()>0) {	
//			threadMessaggi = null;
//			threadMessaggi = threads.get(0);
//			System.out.println("VISUALIZZA THREAD threadMessaggi.getMessaggi().size="+threadMessaggi.getMessaggi().size());	
////			se non è nuovo thread 				
//			if (!threadMessaggi.isNuovoMessaggio()) {
//				System.out.println("VISUALIZZA thread - thread isNuovoMess=false");
//				for (Messaggio m : threadMessaggi.getMessaggi()) {
////						se l'utente (destinatario) non ha letto il messaggio (x non duplicare primary key)
//					if (!messaggioIsLetto(m.getDestinatario().getId(), m))
//					{
//						System.out.println("messaggio non letto da destinatario (me)!");
//						m.getLetto().add(m.getDestinatario());
//					}		
//				}
//			}				
////				controllo se il thread che leggo è contenuto nella lista dei nuovi thread
//			for (Thread t : threadsConNuoviMessaggi) {
//				if (t.getId()==threadMessaggi.getId())	{
//					System.out.println("thread con nuovo mess contenuto in threadsConNuoviMessaggi");
//						threadMessaggi.setNuovoMessaggio(false);
//				}				
//			}	
//			try {
//				serv.merge(threadMessaggi);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		
//			ordinaMessaggiPerData(threadMessaggi.getMessaggi());			
//			nuoviMessaggiThread(utenteBean.getUtente().getId());
//		}
////		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("menuutenteform");
//		contentBean.setContent("messaggioThread.xhtml");		
//	}
	
	
	public void visualizzaThread2 (Thread thread) {
			threadMessaggi = thread;
			System.out.println("VISUALIZZA THREAD threadMessaggi.getMessaggi().size="+threadMessaggi.getMessaggi().size());	
			boolean nuovoMess = false;
			boolean nuovoThread = false;
			//			se non è nuovo thread 				
			if (!threadMessaggi.isNuovoMessaggio()) {
				System.out.println("VISUALIZZA thread - thread isNuovoMess=false");
				for (Messaggio m : threadMessaggi.getMessaggi()) {
//						se l'utente (destinatario) non ha letto il messaggio (x non duplicare primary key)
					if (!messaggioIsLetto(m.getDestinatario().getId(), m))
					{
						System.out.println("messaggio non letto da destinatario (me)!");
						m.getLetto().add(m.getDestinatario());
						nuovoThread = true;
					}		
				}
			}				
//				controllo se il thread che leggo è contenuto nella lista dei nuovi thread
			for (Thread t : threadsConNuoviMessaggi) {
				if (t.getId()==threadMessaggi.getId())	{
					System.out.println("thread con nuovo mess contenuto in threadsConNuoviMessaggi");
						threadMessaggi.setNuovoMessaggio(false);
						nuovoMess = true;
				}				
			}
			if (nuovoMess || nuovoThread) {
				try {
					serv.merge(threadMessaggi);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
			ordinaMessaggiPerData(threadMessaggi.getMessaggi());			
			nuoviMessaggiThread(utenteBean.getUtente().getId());
			contentBean.setContent("messaggioThread.xhtml");		
	}
	
	
	public boolean messaggioRicevutoOSpedito (Messaggio M) {
		if (M.getDestinatario().getId()==utenteBean.getUtente().getId()) {
			return true;
		}
		else {
			return false;
		}
	}
	

	public boolean messaggioIsLetto (int idUtente, Messaggio mess) {
		for (Utente uts : mess.getLetto()) {
			if(uts.getId()==idUtente){
				return true;
			}
		}
		return false;
	}

	
	public void nuoviMessaggiThread (int idUtente) {
		threadsConNuoviMessaggi.clear();
		System.out.println("nuoviMESSAGGIingresso");
		EntityManager em = EMF.createEntityManager();
//		List<Thread> threadsConNuoviMessaggiToT = em
////		.createQuery("from Thread t inner join t.messaggi m inner join m.letto ml where t.nuovoMessaggio = true and m.mittente.id !=:mitt and ml.id!=:mitt")
//		.createQuery("from Thread t where t.nuovoMessaggio = true and t.annuncio is null")
////		.setParameter("mitt", idUtente)
//		.getResultList();	
		List<Thread> threadsConNuoviMessaggiToT = new ArrayList<Thread>();
		List<Thread> threadsInviatiRivutiUtente = new ArrayList<Thread>();
		threadsInviatiRivutiUtente.addAll(threadsInviatiUtente);
		threadsInviatiRivutiUtente.addAll(threadsRicevutiUtente);
		for (Thread thread : threadsInviatiRivutiUtente) {
			if(thread.isNuovoMessaggio()) {
				threadsConNuoviMessaggiToT.add(thread);
			}
		}
		System.out.println("****Threads con isNuovoMessaggio=TRUE_trovati #"+threadsConNuoviMessaggiToT.size());

		int numthr = 0;			
		for (Thread thr : threadsConNuoviMessaggiToT) {
//			controllo se non ho cancellato il thread - in caso lo avessi cancellato, non lo conteggio anche se contiene mess nuovi x me
			if ((thr.getMittentePrimo().getId()==utenteBean.getUtente().getId() && thr.isCancellatoThreadMittente()==false) || (thr.getDestinatarioPrimo().getId()==utenteBean.getUtente().getId() && thr.isCancellatoThreadDestinatario()==false)) {	
				if (threadContieneMessaggiNonLetti(idUtente, thr)) {
					System.out.println("TRUE=contiene"+threadContieneMessaggiNonLetti(idUtente, thr));
					numthr++;
					threadsConNuoviMessaggi.add(thr);
				}
			}
		}
		System.out.println("thread size"+threadsConNuoviMessaggi.size());
		System.out.println("nuovi thread "+numthr);
		nuoviMessaggi = numthr;
	}
	
	
	public boolean threadContieneMessaggiNonLetti (int idUtente, Thread thread) {
		int j=0;
		for (Messaggio mess : thread.getMessaggi()) {
			int i=0;
//			System.out.println("id mess: "+mess.getId());
			for (Utente ut : mess.getLetto()) {
				if (ut.getId()==idUtente) {
					i++;
//					System.out.println("ut.getId()==idUtente__i="+i);
				}
			}
			if (i==0) {
				j++;
//				System.out.println("i==0__j="+j);
			}
		}
		if (j>0) {
			System.out.println("threadContieneMessaggiNonLetti=TRUE_oggetto: "+thread.getOggettoThread());
			return true;
		}
		System.out.println("threadContieneMessaggiNonLetti=FALSE_oggetto: "+thread.getOggettoThread());
		return false;
	}
	
	
	private Map<Integer, Boolean> idThreadsSelezionati = new HashMap<Integer, Boolean>();	
	
	public void cancellaThreadsInviatiSelezionati () {
		for (Thread t : threadsInviatiUtente)  {
			if (idThreadsSelezionati.get(t.getId()).booleanValue()) {
				try {
					t.setCancellatoThreadMittente(true);
					if (t.isCancellatoThreadDestinatario()) {
						serv.delete(t);
					}
					else {
						serv.merge(t);	
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		cercaThreadsInviatiUtente(utenteBean.getUtente().getId());
	}
	
	
	public void cancellaThreadsRicevutiSelezionati () {
		for (Thread t : threadsRicevutiUtente)  {
			if (idThreadsSelezionati.get(t.getId()).booleanValue()) {
				try {
					t.setCancellatoThreadDestinatario(true);
					t.setNuovoMessaggio(false);
					if (t.isCancellatoThreadMittente()) {
						serv.delete(t);
					}
					else {
						serv.merge(t);	
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		nuoviMessaggiThread(utenteBean.getUtente().getId());
		cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
	}
	
	
	IndirectListSorter<Messaggio> messaggiSorter = new IndirectListSorter<Messaggio>();
	
	public void ordinaMessaggiPerData(List<Messaggio> listMess ) {	
		System.out.println("ORDINA MESSAGGI:#"+listMess.size());
 		//ordina per ultimo invio/ricezione
		System.out.println(listMess.getClass().getName());		
		messaggiSorter.sortIndirectList(listMess, new MessaggiComparator());
	}
	
	
	IndirectListSorter<Thread> threadsSorter = new IndirectListSorter<Thread>();	
	
	public void ordinaThreadPerData(List<Thread> listThr ) {	 
		System.out.println("ORDINA THREADS:#"+listThr.size());
 		//ordina per ultimo invio/ricezione
		threadsSorter.sortIndirectList(listThr, new Comparator<Thread>() {
			@Override
			public int compare(Thread u1, Thread u2) {
				int c = u2.getMessaggi().get(u2.getMessaggi().size()-1).getData().compareTo(u1.getMessaggi().get(u1.getMessaggi().size()-1).getData());
//				System.out.println("comparing u2 "+u2.getMessaggi().get(u2.getMessaggi().size()-1).getData()+" and u1 "+u1.getMessaggi().get(u1.getMessaggi().size()-1).getData()+" : "+c);
				return c;
			}
		});
	}
	
	
	IndirectListSorter<Annuncio> annunciSorter = new IndirectListSorter<Annuncio>();	
	
	public void ordinaAnnunciPerData(List<Annuncio> listAnn ) {	 
		System.out.println("ORDINA Annunci:#"+listAnn.size());
 		//ordina per ultima pubblicazione
		annunciSorter.sortIndirectList(listAnn, new AnnunciComparator());
	}
	
	
	public boolean esisteThreadConUtenteTrovato (Utente ut) {
		for (Thread thread : threadsInviatiUtente) {
			if(thread.getDestinatarioPrimo().getId()==ut.getId()) {
				return true;
			}
		}
		for (Thread thread : threadsRicevutiUtente) {
			if(thread.getMittentePrimo().getId()==ut.getId()) {
				return true;
			}
		}
		System.out.println("FALSEEEE");
		return false;
	}
	

	
	
	
	//************GETTERS & SETTERS*******************
	
	
	@ManagedProperty(value = "#{utenteBean}")
	private UtenteBean utenteBean;	

	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;



	public List<Thread> getThreadsConNuoviMessaggi() {
		return threadsConNuoviMessaggi;
	}

	public void setThreadsConNuoviMessaggi(List<Thread> threadsConNuoviMessaggi) {
		this.threadsConNuoviMessaggi = threadsConNuoviMessaggi;
	}

	public Messaggio getMessaggioAggiunto() {
		return messaggioAggiunto;
	}

	public void setMessaggioAggiunto(Messaggio messaggioAggiunto) {
		this.messaggioAggiunto = messaggioAggiunto;
	}

	public Map<Integer, Boolean> getIdThreadsSelezionati() {
		return idThreadsSelezionati;
	}

	public void setIdThreadsSelezionati(Map<Integer, Boolean> idThreadsSelezionati) {
		this.idThreadsSelezionati = idThreadsSelezionati;
	}

	public int getNuoviMessaggi() {
		return nuoviMessaggi;
	}

	public void setNuoviMessaggi(int nuoviMessaggi) {
		this.nuoviMessaggi = nuoviMessaggi;
	}

	public List<Thread> getThreadsInviatiUtente() {
		return threadsInviatiUtente;
	}

	public void setThreadsInviatiUtente(List<Thread> threadsInviatiUtente) {
		this.threadsInviatiUtente = threadsInviatiUtente;
	}

	public List<Thread> getThreadsRicevutiUtente() {
		return threadsRicevutiUtente;
	}

	public void setThreadsRicevutiUtente(List<Thread> threadsRicevutiUtente) {
		this.threadsRicevutiUtente = threadsRicevutiUtente;
	}

	public Thread getThreadMessaggi() {
		return threadMessaggi;
	}

	public void setThreadMessaggi(Thread threadMessaggi) {
		this.threadMessaggi = threadMessaggi;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Messaggio getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(Messaggio messaggio) {
		this.messaggio = messaggio;
	}

	public Thread getThreadEsistente() {
		return threadEsistente;
	}

	public void setThreadEsistente(Thread threadEsistente) {
		this.threadEsistente = threadEsistente;
	}

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