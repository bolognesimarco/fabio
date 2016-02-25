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

import com.bolo.photo.web.entity.Messaggio;
import com.bolo.photo.web.entity.Thread;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;


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
	List<Messaggio> mm = new ArrayList<Messaggio>();
	
	
	public void inviaNuovoMessaggio () {
		System.out.println("INVIA NUOVO MESSAGGIO function");
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
			if (threadEsistente.isCancellatoThreadMittente()) {
				getContentBean().setMessaggio("Thread gi� esistente: messaggio aggiunto!");
				threadEsistente.setCancellatoThreadMittente(false);
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
				serv.merge(threadEsistente);
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
		if(!threadMessaggi.isNuovoMessaggio()){
			for (Messaggio m : threadMessaggi.getMessaggi()) {
				if (m.getMittente().getId()==utenteBean.getUtente().getId()){
					System.out.println("AGGIUNGO MESSAGGIO - aggiungo destinatario in m.letto");
					if (!messaggioIsLetto(mess.getDestinatario().getId(), m.getId())) {
						m.getLetto().add(mess.getDestinatario());
					}
				}
			}
		}
		threadMessaggi.getMessaggi().add(mess);
		threadMessaggi.setNuovoMessaggio(true);
		try {
			serv.merge(threadMessaggi);
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
		.createQuery("from Thread t where t.mittentePrimo.id =:mitt and t.cancellatoThreadMittente = false")
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
		.createQuery("from Thread t where t.destinatarioPrimo.id =:mitt and t.cancellatoThreadDestinatario = false")
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
	
	
	public void visualizzaThread (int idThread) {
		EntityManager em = EMF.createEntityManager();
		List<Thread> threads = em
		.createQuery("from Thread t where t.id =:idThr")
		.setParameter("idThr", idThread)
		.getResultList();	
		if(threads!=null && threads.size()>0) {	
//			setThreadMessaggi(threads.get(0));
			threadMessaggi = null;
			threadMessaggi = threads.get(0);
			System.out.println("VISUALIZZA THREAD threadMessaggi.getMessaggi().size="+threadMessaggi.getMessaggi().size());
//			mm = threadMessaggi.getMessaggi();
//			int j=0;
//			for (Messaggio m : mm) {	
//				System.out.println("NON ORDINATO-Messaggio #("+j+")-data"+m.getData());	
//				j++;
//			}
//			ordinaMessaggiPerData(mm);
//			int i =0;
//			for (Messaggio m : mm) {	
//				System.out.println("ORDINATO-Messaggio #("+i+")-data"+m.getData());	
//				i++;
//			}
			
//			se non � nuovo thread 				
			if (!threadMessaggi.isNuovoMessaggio()) {
				System.out.println("VISUALIZZA thread - thread isNuovoMess=false");
				for (Messaggio m : threadMessaggi.getMessaggi()) {
//					se utente ha ricevuto il messaggio			
//					if(m.getDestinatario().getId()==utenteBean.getUtente().getId()){

//						se l'utente (destinatario) non ha letto il messaggio (x non duplicare primary key)
						if (!messaggioIsLetto(m.getDestinatario().getId(), m.getId()))
						{
							System.out.println("messaggio non letto da destinatario (me)!");
							m.getLetto().add(m.getDestinatario());
						}
//					}
//					utente ha spedito il messaggio
//					else
//					{
////						se L'altro utente non ha letto il messaggio (x non duplicare primary key)
//						if (!messaggioIsLetto(m.getDestinatario().getId(), m.getId()))
//						{
//							System.out.println("messaggio non letto da destinatario (l'altro)");
//							m.getLetto().add(m.getDestinatario());
//						}
//					}
					
				}
			}
				
//				controllo se il thread che leggo � contenuto nella lista dei nuovi thread
			for (Thread t : threadsConNuoviMessaggi) {
				if (t.getId()==threadMessaggi.getId())	{
					System.out.println("thread con nuovo mess contenuto in threadsConNuoviMessaggi");
						threadMessaggi.setNuovoMessaggio(false);
				}
//				else {
//					System.out.println("thread con nuovo mess NON contenuto in threadsConNuoviMessaggi");
//					for (Messaggio m : threadMessaggi.getMessaggi()) {
//						System.out.println("messaggio letto da utenteeeee:"+messaggioIsLetto(utenteBean.getUtente().getId(), m.getId()));
////						se utente ha ricevuto il messaggio
//						if(m.getDestinatario().getId()==utenteBean.getUtente().getId()){
////							se utente non ha letto il messaggio (x non duplicare primary key)
//							if (!messaggioIsLetto(utenteBean.getUtente().getId(), m.getId()))
//							{
//								System.out.println("messaggio letto da utenteeeee:"+messaggioIsLetto(utenteBean.getUtente().getId(), m.getId()));
//								m.getLetto().add(utenteBean.getUtente());
//							}
//						}
//					}
//				}
					
			}	
			try {
				serv.merge(threadMessaggi);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nuoviMessaggiThread(utenteBean.getUtente().getId());
			}
//			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("menuutenteform");
		contentBean.setContent("messaggioThread.xhtml");		
	}
	
	
	public boolean messaggioIsLetto (int idUtente, int idMess) {
//		System.out.println("messaggioIsLetto?");
		EntityManager em = EMF.createEntityManager();
		List<Messaggio> mess = em
		.createQuery("from Messaggio m inner join m.letto ml where m.id=:idmess and ml.id=:idut")
		.setParameter("idut", idUtente)
		.setParameter("idmess", idMess)
		.getResultList();	
		if (mess!=null && mess.size()>0) {
			return true;
		}
		return false;
	}

	
	public void nuoviMessaggiThread (int idUtente) {
		threadsConNuoviMessaggi.clear();
		System.out.println("nuoviMESSAGGIingresso");
		EntityManager em = EMF.createEntityManager();
		List<Thread> threadsConNuoviMessaggiToT = em
//		.createQuery("from Thread t left join t.messaggi m left join m.letto ml where t.nuovoMessaggio = true and m.mittente.id !=:mitt and ml.id!=:mitt")
		.createQuery("from Thread t where t.nuovoMessaggio = true")
//		.setParameter("mitt", idUtente)
		.getResultList();	
		System.out.println("****Threads con isNuovoMessaggio=TRUE_trovati #"+threadsConNuoviMessaggiToT.size());

		int numthr = 0;	
		for (Thread thr : threadsConNuoviMessaggiToT) {
			if (threadContieneMessaggiNonLetti(idUtente, thr)) {
				System.out.println("TRUE=contiene"+threadContieneMessaggiNonLetti(idUtente, thr));
				numthr++;
				threadsConNuoviMessaggi.add(thr);
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
			System.out.println("threadContieneMessaggiNonLetti=TRUE");
			return true;
		}
		System.out.println("threadContieneMessaggiNonLetti=FALSE");
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
	
	
	
//	public void ordinaMPerData(List<Messaggio> msgs){
//		System.out.println("in ordinaMPerData");
//		Collections.sort(msgs, new Comparator<Messaggio>() {
//
//			@Override
//			public int compare(Messaggio o1, Messaggio o2) {
//				System.out.println("in compare di ordinaMPerData");
//				return o2.getData().compareTo(o1.getData());
//			}
//		});
//	}
	
	
	public static void main(String[] aa) throws Exception{
		List<Messaggio>  msgs = new ArrayList<Messaggio>();
		Messaggio m1 = new Messaggio();
		m1.setData(new Date());
		m1.setId(1);
		msgs.add(m1);
		
		java.lang.Thread.sleep(5000L);
		
		Messaggio m2 = new Messaggio();
		m2.setData(new Date());
		m2.setId(2);
		msgs.add(m2);
		
		ThreadBean tb = new ThreadBean();
		tb.ordinaMessaggiPerData(msgs);
		
		System.out.println(msgs.get(0).getId());
	}
	
	
	public void ordinaMessaggiPerData(List<Messaggio> listMess ) {	
		System.out.println("ORDINA MESSAGGI:#"+listMess.size());
 		//ordina per ultimo invio/ricezione
		Collections.sort(listMess,  new Comparator<Messaggio>() {
			@Override
			public int compare(Messaggio u1, Messaggio u2) {
				System.out.println("in compare: ");
					int c = u2.getData().compareTo(u1.getData());
					System.out.println("comparing u2 "+u2.getData()+" and u1 "+u1.getData()+" : "+c);
				return c;
			}
		});
//		   return null;
	}
	
	
	public void ordinaThreadPerData(List<Thread> listThr ) {	 
		System.out.println("ORDINA THREADS:#"+listThr.size());
 		//ordina per ultimo invio/ricezione
		Collections.sort(listThr, new Comparator<Thread>() {
			@Override
			public int compare(Thread u1, Thread u2) {
				int c = u2.getMessaggi().get(u2.getMessaggi().size()-1).getData().compareTo(u1.getMessaggi().get(u1.getMessaggi().size()-1).getData());
				System.out.println("comparing u2 "+u2.getMessaggi().get(u2.getMessaggi().size()-1).getData()+" and u1 "+u1.getMessaggi().get(u1.getMessaggi().size()-1).getData()+" : "+c);
				return c;
			}
		});
//	   return null;
	}
	
	
	
	//************GETTERS & SETTERS*******************
	
	
	@ManagedProperty(value = "#{utenteBean}")
	private UtenteBean utenteBean;	

	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;

	


	public List<Messaggio> getMm() {
		return mm;
	}

	public void setMm(List<Messaggio> mm) {
		this.mm = mm;
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