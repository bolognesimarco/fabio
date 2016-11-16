package com.bolo.photoshooters.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.bolo.photo.web.entity.Annuncio;
import com.bolo.photo.web.entity.EmailDaInviare;
import com.bolo.photo.web.entity.Messaggio;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photo.web.entity.Thread;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.util.AnnunciComparator;
import com.bolo.photoshooters.util.IndirectListSorter;
import com.bolo.photoshooters.util.MessaggiComparator;

@ManagedBean
@SessionScoped
public class AnnuncioBean {

	private ServiziComuni serv = new ServiziComuniImpl();
	private Messaggio messaggioAggiunto = new Messaggio();
	List<Annuncio> annunciUtente = new ArrayList<Annuncio>();
	List<Annuncio> annunciSito = new ArrayList<Annuncio>();
	List<Annuncio> annunciRispostiDaUtente = new ArrayList<Annuncio>();
	private Annuncio nuovoAnnuncio = new Annuncio();
	private Messaggio messaggioNuovoAnnuncio = new Messaggio();
	private Annuncio annuncioEsistente = new Annuncio();
	private Annuncio annuncioPubblicato = new Annuncio();
	private Annuncio annuncioAltrui = new Annuncio();
	Messaggio messaggioRispostaAnnuncio = new Messaggio();
	Thread threadAnnuncioPubblicato = new Thread();
	Thread threadRispostaAnnuncio = new Thread();
	List<Thread> threadsAnnunciConNuoviMessaggi = new ArrayList<Thread>();
	private int nuoviMessaggiAnnunci = 0;
	private boolean tutteRegioni = false;
	

	

	public boolean messaggioIsLetto (int idUtente, Messaggio mess) {
		for (Utente uts : mess.getLetto()) {
			if(uts.getId()==idUtente){
				return true;
			}
		}
		return false;
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
				System.out.println("comparing u2 "+u2.getMessaggi().get(u2.getMessaggi().size()-1).getData()+" and u1 "+u1.getMessaggi().get(u1.getMessaggi().size()-1).getData()+" : "+c);
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
	

	public void pubblicaNuovoAnnuncio () {
		System.out.println("PUBBLICA NUOVO ANNUNCIO function"+messaggioNuovoAnnuncio.getOggetto());
		Annuncio ann = new Annuncio();
		Thread thr = new Thread();	
		Messaggio mess = new Messaggio();
		thr.setMittentePrimo(utenteBean.getUtente());
		thr.setDestinatarioPrimo(utenteBean.getUtente());
		thr.setOggettoThread(messaggioNuovoAnnuncio.getOggetto());
		mess.setMessaggio(messaggioNuovoAnnuncio.getMessaggio());
		mess.setMittente(utenteBean.getUtente());
		mess.setDestinatario(utenteBean.getUtente());
		mess.setOggetto(messaggioNuovoAnnuncio.getOggetto());
		mess.setThread(thr);
		mess.getLetto().add(utenteBean.getUtente());
		Date ora = new Date();
		mess.setData(ora);
		thr.getMessaggi().add(mess);
		thr.setAnnuncio(ann);
		thr.setNuovoMessaggio(false);
		ann.setCitt‡Annuncio(nuovoAnnuncio.getCitt‡Annuncio());
		for (RegioneItaliana reg : nuovoAnnuncio.getRegioniAnnuncio()) {
			if(reg!=null) {
				ann.getRegioniAnnuncio().add(reg);
			}
		}
		if (utenteBean.getRegion()!="" && !controllaRegione(utenteBean.getRegion())) {
			System.out.println("nuovo annuncio REGIONE===="+utenteBean.getRegion());
			ann.getRegioniAnnuncio().add(RegioneItaliana.valueOf(utenteBean.getRegion()));
		}
		if (utenteBean.getRegion()=="") {
			try {
				utenteBean.suggerisciCitt‡(nuovoAnnuncio.getCitt‡Annuncio());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!controllaRegione(utenteBean.getRegion())) {
				System.out.println("nuovo annuncio REGIONE===="+utenteBean.getRegion());
				ann.getRegioniAnnuncio().add(RegioneItaliana.valueOf(utenteBean.getRegion()));
			}
		}
		ann.setProponente(utenteBean.getUtente());
		ann.getRisposte().add(thr);
		System.out.println("oggetto........."+thr.getOggettoThread());
		System.out.println("oggetto mess........."+messaggioNuovoAnnuncio.getOggetto());
		try {
			serv.persist(ann);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		nuovoAnnuncio.getRegioniAnnuncio().clear();
		nuovoAnnuncio.setCitt‡Annuncio(null);
//		setMessaggioNuovoAnnuncio(null);
		messaggioNuovoAnnuncio = new Messaggio();
		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("headerform");
		contentBean.setContent("annunci.xhtml");
		contentBean.setMessaggio("annuncio nuovo");
	}

	
	public void ripubblicaAnnuncio(Annuncio ann) {
		Date ora = new Date();
		ann.getRisposte().get(0).getMessaggi().get(ann.getRisposte().get(0).getMessaggi().size()-1).setData(ora);
		try {
			serv.merge(ann);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void cercaAnnunciRispostiDaUtente (int idUtente) {
		EntityManager em = EMF.createEntityManager();
		annunciRispostiDaUtente = em
		.createQuery("from Annuncio a inner join a.risposte ar where a.proponente.id <>:prop and ar.mittentePrimo.id =:prop and ar.cancellatoThreadMittente = false")
		.setParameter("prop", idUtente)
		.getResultList();
		ordinaAnnunciPerData(annunciRispostiDaUtente);
		System.out.println("annunciRispostiDaUtente size="+annunciRispostiDaUtente.size());
	}
	
	
	public void cercaAnnunciPubblicatiDaUtente (int idUtente) {
		EntityManager em = EMF.createEntityManager();
		annunciUtente = em
		.createQuery("from Annuncio a where a.proponente.id =:prop")
		.setParameter("prop", idUtente)
		.getResultList();
		ordinaAnnunciPerData(annunciUtente);	
		System.out.println("annunciUtente size="+annunciUtente.size());
	}
	
	
	private Map<Integer, Boolean> idAnnunciSelezionati = new HashMap<Integer, Boolean>();	
	
	public void cancellaAnnunciPubblicatiSelezionati () {
		System.out.println("Cancella annunci pubblicati START");
		for (Annuncio a : annunciUtente)  {
			if (idAnnunciSelezionati.get(a.getId()).booleanValue()) {
				System.out.println("delete annuncio ID="+a.getId());
				try {
					serv.delete(a);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
	}

	
	public void cancellaThreadsAnnunciRispostiSelezionati () {
		System.out.println("Cancella annunci START");
		for (Annuncio a : annunciRispostiDaUtente)  {
			if (idAnnunciSelezionati.get(a.getId()).booleanValue()) {
				System.out.println("delete thread risposta annuncio ID="+a.getId());
				for (Thread thread : a.getRisposte()) {
					if(thread.getMittentePrimo().getId()==utenteBean.getUtente().getId()) {
						System.out.println("thr da cancellare id="+thread.getId());
						try {
							thread.setCancellatoThreadMittente(true);
//							thread.setNuovoMessaggio(false);
							if (thread.isCancellatoThreadDestinatario()) {
								serv.delete(thread);
								System.out.println("delete");
							}
							else {
								serv.merge(thread);	
								System.out.println("merge");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
				}
			}
		}
		nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
	}
	
	
	public boolean annuncioContieneRisposteNonLette (int idUtente, Annuncio ann) {
		for (Thread thr : ann.getRisposte()) {
			int j=0;
	//		System.out.println("annuncioContieneRisposteNonLette size="+ann.getRisposte().get(0).getMessaggi().size());
			for (Messaggio mess : thr.getMessaggi()) {
//				escludo il primo messaggio che Ë quello dell'annuncio, dove mittente=destinatario
				if(mess.getMittente().getId()!=mess.getDestinatario().getId()) {
					int i=0;
					System.out.println("id mess: "+mess.getId());
					for (Utente ut : mess.getLetto()) {
						if (ut.getId()==idUtente) {
							i++;
							System.out.println("ut.getId()==idUtente__i="+i);
						}
					}
		//			i=0 -> ci sono mess non letti
					if (i==0) {
						j++;
						System.out.println("mess non letto ID="+mess.getId());
					}
				}
			}
			if (j>0) {
				System.out.println("annuncioContieneRisposteNonLette=TRUE_oggetto: "+ann.getRisposte().get(0).getOggettoThread());
				return true;
			}
		}
		System.out.println("annuncioContieneRisposteNonLette=FALSE_oggetto: "+ann.getRisposte().get(0).getOggettoThread());
		return false;
	}
	
	
	public boolean annuncioContieneThreadNuovo (Annuncio ann) {
			for (Thread thr : ann.getRisposte()) {
				if(thr.isNuovoMessaggio())
					return true;
			}
		return false;
	}
	
	
	public void visualizzaAnnuncioPubblicato (Annuncio ann) {
		setAnnuncioPubblicato(ann);
		setThreadAnnuncioPubblicato(null);
		System.out.println("annunciopubblicato id="+ann.getId());
		contentBean.setMessaggio(null);
		contentBean.setContent("annuncioPubblicato.xhtml");		
	}
	
	
	public void visualizzaAnnuncioAltrui (Annuncio ann) {
		System.out.println("visualizzaAnnuncioAltrui STARTTT  proponente=="+ann.getProponente().getUsername());
		annuncioAltrui = ann;
//		ordinaThreadPerData(ann.getRisposte());
		esisteRispostaAnnuncio(utenteBean.getUtente(), ann);
		System.out.println("threadRispostaAnnuncio starttt ID="+threadRispostaAnnuncio.getId());
		boolean nuovoThread = false;
		boolean nuovoMess = false;
		if (!threadRispostaAnnuncio.isNuovoMessaggio()) {
			System.out.println("thread non nuovo");
			for (Messaggio m : threadRispostaAnnuncio.getMessaggi()) {
//					se l'utente (destinatario) non ha letto il messaggio (x non duplicare primary key)
				if (!messaggioIsLetto(m.getDestinatario().getId(), m))
				{
					System.out.println("messaggio annuncioAltrui non letto da destinatario (me)!");
					m.getLetto().add(m.getDestinatario());
					nuovoThread = true;
				}		
			}
		}				
//			controllo se il thread che leggo Ë contenuto nella lista dei nuovi thread
		System.out.println("threadsAnnunciConNuoviMessaggi size= "+threadsAnnunciConNuoviMessaggi.size());
		for (Thread t : threadsAnnunciConNuoviMessaggi) {
			System.out.println("threadsAnnunciConNuoviMessaggi ID="+t.getId());
			System.out.println("threadRispostaAnnuncio ID="+threadRispostaAnnuncio.getId());
			if (t.getId()==threadRispostaAnnuncio.getId())	{
				System.out.println("thread annuncioAltrui con nuovo mess contenuto in threadsAnnunciConNuoviMessaggi");
				threadRispostaAnnuncio.setNuovoMessaggio(false);
				nuovoMess = true;
			}				
		}
		if (nuovoMess || nuovoThread) {
			try {
				serv.merge(threadRispostaAnnuncio);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		ordinaThreadPerData(ann.getRisposte());
		ordinaMessaggiPerData(threadRispostaAnnuncio.getMessaggi());
		if (utenteBean.getUtente()!=null) {
				nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
		}
		messaggioRispostaAnnuncio = new Messaggio();
		contentBean.setMessaggio(null);
		contentBean.setContent("annuncioAltrui.xhtml");		
	}
	
	
	public void visualizzaThreadAnnuncioPubblicato (Thread thread) {
		System.out.println("visualizzaThreadAnnuncioPubblicato START");
		threadAnnuncioPubblicato = thread;
		boolean nuovoThread = false;
		boolean nuovoMess = false;
//		se non Ë nuovo thread 			
		System.out.println("threadAnnuncioPubblicato nuovo????"+threadAnnuncioPubblicato.isNuovoMessaggio());
		if (!threadAnnuncioPubblicato.isNuovoMessaggio()) {
			System.out.println("thread non ha nuovimessaggi");
			for (Messaggio m : threadAnnuncioPubblicato.getMessaggi()) {
//					se l'utente (destinatario) non ha letto il messaggio (x non duplicare primary key)
				if (!messaggioIsLetto(m.getDestinatario().getId(), m))
				{
					System.out.println("messaggio non letto da destinatario (me)!");
					m.getLetto().add(m.getDestinatario());
					nuovoThread = true;
				}		
			}
		}				
//			controllo se il thread che leggo Ë contenuto nella lista dei nuovi thread
		System.out.println("threadsAnnunciConNuoviMessaggi size= "+threadsAnnunciConNuoviMessaggi.size());
		for (Thread t : threadsAnnunciConNuoviMessaggi) {;
			if (t.getId()==threadAnnuncioPubblicato.getId())	{
				System.out.println("thread annuncio con nuovo mess contenuto in threadsAnnunciConNuoviMessaggi");
				threadAnnuncioPubblicato.setNuovoMessaggio(false);
				nuovoMess = true;
			}				
		}	
		if (nuovoMess || nuovoThread) {
			try {
				serv.merge(threadAnnuncioPubblicato);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ordinaMessaggiPerData(threadAnnuncioPubblicato.getMessaggi());			
		nuoviMessaggiThreadsAnnuncio(utenteBean.getUtente().getId());
		contentBean.setContent("annuncioThread.xhtml");		
	}
	
	
	public void nuoviMessaggiThreadsAnnuncio (int idUtente) {
		threadsAnnunciConNuoviMessaggi.clear();
		System.out.println("nuoviMESSAGGIANNUNCIIIIIingresso");
		
		List<Annuncio> annunciConNuoviMessaggiInThreadsToT = new ArrayList<Annuncio>();
		List<Annuncio> annunciTotUtente = new ArrayList<Annuncio>();
		annunciTotUtente.addAll(annunciUtente);
		annunciTotUtente.addAll(annunciRispostiDaUtente);
		if(annunciUtente.size()>0){
			System.out.println("annunciUtente.risposte() size="+annunciTotUtente.get(0).getRisposte().size());
		}

		if (annunciRispostiDaUtente.size()>0) {
			System.out.println("annunciRispostiDaUtente.risposte() size="+annunciRispostiDaUtente.get(0).getRisposte().size());
		}
		System.out.println("annunciTotUtente size="+annunciTotUtente.size());
		for (Annuncio annuncio : annunciTotUtente) {
			System.out.println("annunciTotUtente id="+annuncio.getId());
			System.out.println("annunciTotUtente thread.size===="+annuncio.getRisposte().size());
			for (Thread thr : annuncio.getRisposte()) {
				System.out.println("thread annuncio id="+thr.getId());
				if (thr.isNuovoMessaggio()) {
					System.out.println("isNuovoMessaggio== TRUEEE id="+thr.getId());
					annunciConNuoviMessaggiInThreadsToT.add(annuncio);
				}
			}
		}
		System.out.println("****ANNUNCIO Pubblicati+Risposti #"+annunciConNuoviMessaggiInThreadsToT.size());

		int numthr = 0;		
		for (Annuncio ann : annunciConNuoviMessaggiInThreadsToT) {	
			for (Thread thr : ann.getRisposte()) {
				System.out.println("THREAD ID="+thr.getId());
	//			controllo se non ho cancellato il thread - in caso lo avessi cancellato, non lo conteggio anche se contiene mess nuovi x me
				if ((thr.getMittentePrimo().getId()==utenteBean.getUtente().getId() && thr.isCancellatoThreadMittente()==false) || (thr.getDestinatarioPrimo().getId()==utenteBean.getUtente().getId() && thr.isCancellatoThreadDestinatario()==false)) {	
					if (threadContieneMessaggiNonLetti(idUtente, thr)) {
						System.out.println("TRUE=contiene"+threadContieneMessaggiNonLetti(idUtente, thr));
						numthr++;
						threadsAnnunciConNuoviMessaggi.add(thr);
					}
				}
			}
		}
		System.out.println("threads annunci con messaggi non letti size"+threadsAnnunciConNuoviMessaggi.size());
		nuoviMessaggiAnnunci = numthr;
		System.out.println("nuoviMessaggiAnnunci==="+nuoviMessaggiAnnunci);
	}
	
	
	public void aggiungiRispostaThreadAnnuncio() {
		System.out.println("AGGIUNGI RISPOSTA ANNUNCIO function");
		Messaggio mess = new Messaggio();
		mess.setOggetto(threadAnnuncioPubblicato.getOggettoThread());
		mess.setMessaggio(messaggioAggiunto.getMessaggio());
		mess.setMittente(utenteBean.getUtente());
		mess.setDestinatario(threadAnnuncioPubblicato.getMittentePrimo());
		mess.setThread(threadAnnuncioPubblicato);
		Date ora = new Date();
		mess.setData(ora);
		mess.getLetto().add(utenteBean.getUtente());
		threadAnnuncioPubblicato.getMessaggi().add(mess);
		if(!threadAnnuncioPubblicato.isNuovoMessaggio()){
			for (Messaggio m : threadAnnuncioPubblicato.getMessaggi()) {
//				System.out.println("MESSAGGIO id="+m.getId()+" - UTENTE (Destinatario) id="+m.getDestinatario().getId());
				if (!messaggioIsLetto(utenteBean.getUtente().getId(), m)) {
//					System.out.println("AGGIUNGO MESSAGGIO - UTENTE (Destinatario) id="+m.getDestinatario().getId()+" in m.letto messID="+m.getId());
					m.getLetto().add(utenteBean.getUtente());
				}
			}
		}
		threadAnnuncioPubblicato.setNuovoMessaggio(true);
		try {
			serv.merge(threadAnnuncioPubblicato);
			if(threadAnnuncioPubblicato.getMittentePrimo().isMailNuovaRispostaAnnuncio()) {
				EmailDaInviare email = new EmailDaInviare();
				email.setTipoEmail(1);
				email.setUtenteSender(utenteBean.getUtente());
				email.setEmail(threadAnnuncioPubblicato.getMittentePrimo().getEmail());
				serv.persist(email);
//				MailSender.sendNuovaRispostaAnnuncioAltruiMail(threadAnnuncioPubblicato.getMittentePrimo().getEmail(), utenteBean.getUtente().getUsername());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		messaggioAggiunto.setMessaggio(null);
		mess = null;
		setThreadAnnuncioPubblicato(null);
		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
		contentBean.setContent("annunci.xhtml");
		contentBean.setMessaggio("risposta annuncio aggiunta");
	}
	
	
	public void rispondiAnnuncio(Annuncio a) {
		System.out.println("RISPONDI ANNUNCIO function");
//		se Ë la mia prima risposta all'annuncio - creo nuovo thread
		if(!esisteRispostaAnnuncio(utenteBean.getUtente(), a)) {
			Thread thr = new Thread();	
			Messaggio mess = new Messaggio();
			thr.setMittentePrimo(utenteBean.getUtente());
			thr.setDestinatarioPrimo(a.getProponente());
			thr.setOggettoThread(a.getRisposte().get(0).getOggettoThread());
			mess.setMessaggio(messaggioRispostaAnnuncio.getMessaggio());
			mess.setMittente(utenteBean.getUtente());
			mess.setDestinatario(a.getProponente());
			mess.setOggetto(a.getRisposte().get(0).getOggettoThread());
			mess.setThread(thr);
			mess.getLetto().add(utenteBean.getUtente());
			Date ora = new Date();
			mess.setData(ora);
			thr.getMessaggi().add(mess);
			thr.setAnnuncio(a);
			thr.setNuovoMessaggio(true);
			a.getRisposte().add(thr);
			try {
				serv.merge(a);
				if(a.getProponente().isMailNuovaRispostaAnnuncio()) {
					EmailDaInviare email = new EmailDaInviare();
					email.setTipoEmail(1);
					email.setUtenteSender(utenteBean.getUtente());
					email.setEmail(a.getProponente().getEmail());
					serv.persist(email);
//					MailSender.sendNuovaRispostaAnnuncioMail(a.getProponente().getEmail(), utenteBean.getUtente().getUsername());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
		} else {
//			gi‡ risposto all'annuncio, uso lo stesso thread
			System.out.println("gi‡ risposto all'annuncio, uso lo stesso thread");
			Messaggio mess = new Messaggio();
			mess.setMessaggio(messaggioRispostaAnnuncio.getMessaggio());
			mess.setMittente(utenteBean.getUtente());
			mess.setDestinatario(a.getProponente());
			mess.setOggetto(a.getRisposte().get(0).getOggettoThread());
			mess.setThread(threadRispostaAnnuncio);
			mess.getLetto().add(utenteBean.getUtente());
			Date ora = new Date();
			mess.setData(ora);
			if(!threadRispostaAnnuncio.isNuovoMessaggio()){
				for (Messaggio m : threadRispostaAnnuncio.getMessaggi()) {
//					System.out.println("MESSAGGIO id="+m.getId()+" - UTENTE (Destinatario) id="+m.getDestinatario().getId());
					if (!messaggioIsLetto(m.getDestinatario().getId(), m)) {
//						System.out.println("AGGIUNGO MESSAGGIO - UTENTE (Destinatario) id="+m.getDestinatario().getId()+" in m.letto messID="+m.getId());
						m.getLetto().add(m.getDestinatario());
					}
				}
			}
			threadRispostaAnnuncio.getMessaggi().add(mess);
			threadRispostaAnnuncio.setNuovoMessaggio(true);
//			a.getRisposte().add(threadRispostaAnnuncio);
			try {
				serv.merge(a);
				if(a.getProponente().isMailNuovaRispostaAnnuncio()) {
					EmailDaInviare email = new EmailDaInviare();
					email.setTipoEmail(1);
					email.setUtenteSender(utenteBean.getUtente());
					email.setEmail(a.getProponente().getEmail());
					serv.persist(email);
//					MailSender.sendNuovaRispostaAnnuncioMail(a.getProponente().getEmail(), utenteBean.getUtente().getUsername());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
		}
		messaggioRispostaAnnuncio.setMessaggio(null);
		cercaAnnunciPubblicatiDaUtente(utenteBean.getUtente().getId());
		cercaAnnunciRispostiDaUtente(utenteBean.getUtente().getId());
		contentBean.setContent("annunci.xhtml");
		contentBean.setMessaggio("risposto ad annuncio");
	}
	
	
	public boolean esisteRispostaAnnuncio (Utente ut, Annuncio a) {
		System.out.println("esisteRispostaAnnuncio START");
		if (ut!=null){
			for (Thread thr : a.getRisposte()) {
				System.out.println("a.risposte id===="+thr.getId());
				System.out.println("a.risposte(0) id===="+a.getRisposte().get(a.getRisposte().size()-1).getId());
//				if (thr.getMittentePrimo().getId()==ut.getId() && (thr.getId()!=a.getRisposte().get(a.getRisposte().size()-1).getId())) {
				if (thr.getMittentePrimo().getId()==ut.getId() && (thr.getMittentePrimo().getId()!=thr.getDestinatarioPrimo().getId())) {
					setThreadRispostaAnnuncio(thr);
					System.out.println("esisteRispostaAnnuncio =TRUE");
					System.out.println("ThreadRispostaAnnuncio id=="+threadRispostaAnnuncio.getId());
					System.out.println("thr id=="+thr.getId());
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void cercaAnnunciSito() {
		EntityManager em = EMF.createEntityManager();
		String hqlstart = "from Annuncio a ";
		String hqlcerca = "";
		String hql = "";
		int i = 0;
		Boolean utenteLoggato = false;
		if(utenteBean.tipoMembershipUtente()!=0){
			hqlcerca += " a.proponente.id<>:idUt ";
			utenteLoggato = true;
			i++;
		}
		if (i>0){
			hql = hqlstart + "where" + hqlcerca; 
		}
		else {
			hql = hqlstart; 
		}
		Query q = em.createQuery(hql, Annuncio.class);
		if(utenteLoggato){
			q.setParameter("idUt",utenteBean.getUtente().getId());
		}
		System.out.println("ANNUNCI SITO hql="+hql);
		annunciSito = q.getResultList();
	}
	
	
	public void selezionaTutteRegioniAnnuncio() {
		if (isTutteRegioni()) {
			nuovoAnnuncio.getRegioniAnnuncio().clear();
			List<RegioneItaliana> List =
	                new ArrayList<RegioneItaliana>(EnumSet.allOf(RegioneItaliana.class));
			nuovoAnnuncio.setRegioniAnnuncio(List);
		} else {
			nuovoAnnuncio.getRegioniAnnuncio().clear();
		}
	}
	
	
	public boolean controllaRegione (String reg) {
		for (RegioneItaliana regUtente : nuovoAnnuncio.getRegioniAnnuncio()) {
			if (regUtente.toString().equals(reg)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	//************GETTERS & SETTERS*******************
	
	
	@ManagedProperty(value = "#{utenteBean}")
	private UtenteBean utenteBean;	

	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;

	

	
	

	public boolean isTutteRegioni() {
		return tutteRegioni;
	}

	public void setTutteRegioni(boolean tutteRegioni) {
		this.tutteRegioni = tutteRegioni;
	}

	public Thread getThreadRispostaAnnuncio() {
		return threadRispostaAnnuncio;
	}

	public void setThreadRispostaAnnuncio(Thread threadRispostaAnnuncio) {
		this.threadRispostaAnnuncio = threadRispostaAnnuncio;
	}

	public List<Annuncio> getAnnunciRispostiDaUtente() {
		return annunciRispostiDaUtente;
	}

	public void setAnnunciRispostiDaUtente(List<Annuncio> annunciRispostiDaUtente) {
		this.annunciRispostiDaUtente = annunciRispostiDaUtente;
	}

	public Messaggio getMessaggioRispostaAnnuncio() {
		return messaggioRispostaAnnuncio;
	}

	public void setMessaggioRispostaAnnuncio(Messaggio messaggioRispostaAnnuncio) {
		this.messaggioRispostaAnnuncio = messaggioRispostaAnnuncio;
	}

	public List<Annuncio> getAnnunciSito() {
		return annunciSito;
	}

	public void setAnnunciSito(List<Annuncio> annunciSito) {
		this.annunciSito = annunciSito;
	}

	public Annuncio getAnnuncioAltrui() {
		return annuncioAltrui;
	}

	public void setAnnuncioAltrui(Annuncio annuncioAltrui) {
		this.annuncioAltrui = annuncioAltrui;
	}

	public Thread getThreadAnnuncioPubblicato() {
		return threadAnnuncioPubblicato;
	}

	public void setThreadAnnuncioPubblicato(Thread threadAnnuncioPubblicato) {
		this.threadAnnuncioPubblicato = threadAnnuncioPubblicato;
	}

	public int getNuoviMessaggiAnnunci() {
		return nuoviMessaggiAnnunci;
	}

	public void setNuoviMessaggiAnnunci(int nuoviMessaggiAnnunci) {
		this.nuoviMessaggiAnnunci = nuoviMessaggiAnnunci;
	}

	public List<Thread> getThreadsAnnunciConNuoviMessaggi() {
		return threadsAnnunciConNuoviMessaggi;
	}

	public void setThreadsAnnunciConNuoviMessaggi(
			List<Thread> threadsAnnunciConNuoviMessaggi) {
		this.threadsAnnunciConNuoviMessaggi = threadsAnnunciConNuoviMessaggi;
	}

	public Annuncio getAnnuncioPubblicato() {
		return annuncioPubblicato;
	}

	public void setAnnuncioPubblicato(Annuncio annuncioPubblicato) {
		this.annuncioPubblicato = annuncioPubblicato;
	}

	public Map<Integer, Boolean> getIdAnnunciSelezionati() {
		return idAnnunciSelezionati;
	}

	public void setIdAnnunciSelezionati(Map<Integer, Boolean> idAnnunciSelezionati) {
		this.idAnnunciSelezionati = idAnnunciSelezionati;
	}

	public List<Annuncio> getAnnunciUtente() {
		return annunciUtente;
	}

	public void setAnnunciUtente(List<Annuncio> annunciUtente) {
		this.annunciUtente = annunciUtente;
	}

	public Annuncio getNuovoAnnuncio() {
		return nuovoAnnuncio;
	}

	public void setNuovoAnnuncio(Annuncio nuovoAnnuncio) {
		this.nuovoAnnuncio = nuovoAnnuncio;
	}

	public Messaggio getMessaggioNuovoAnnuncio() {
		return messaggioNuovoAnnuncio;
	}

	public void setMessaggioNuovoAnnuncio(Messaggio messaggioNuovoAnnuncio) {
		this.messaggioNuovoAnnuncio = messaggioNuovoAnnuncio;
	}

	public Annuncio getAnnuncioEsistente() {
		return annuncioEsistente;
	}

	public void setAnnuncioEsistente(Annuncio annuncioEsistente) {
		this.annuncioEsistente = annuncioEsistente;
	}

	public Messaggio getMessaggioAggiunto() {
		return messaggioAggiunto;
	}

	public void setMessaggioAggiunto(Messaggio messaggioAggiunto) {
		this.messaggioAggiunto = messaggioAggiunto;
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
