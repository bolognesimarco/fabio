package com.bolo.photoshooters.web;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.bolo.photo.web.entity.Messaggio;
import com.bolo.photo.web.entity.Thread;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;


@ManagedBean
@SessionScoped
public class ThreadBean {

	private ServiziComuni serv = new ServiziComuniImpl();
    List<Thread> threadsInviatiUtente = new ArrayList<Thread>();
    List<Thread> threadsRicevutiUtente = new ArrayList<Thread>();
    Thread threadMessaggi = new Thread();
	private Thread thread = new Thread();
	private Messaggio messaggio = new Messaggio();
	private Thread threadEsistente = new Thread();
	private int nuoviMessaggi = 0;

	
	
	public void inviaMessaggio () {
		System.out.println("INVIA MESSAGGIO function");
		if (!esisteThreadMessaggi(utenteBean.getUtente().getId(), thread.getDestinatarioPrimo().getId(), messaggio.getOggetto())){
			System.out.println("INVIA MESSAGGIO==nuovo threadddddd");
			Thread thr = new Thread();	
			Messaggio mess = new Messaggio();
			thr.setMittentePrimo(utenteBean.getUtente());
			thr.setDestinatarioPrimo(thread.getDestinatarioPrimo());
			thr.setOggettoThread(messaggio.getOggetto());
			mess.setMessaggio(messaggio.getMessaggio());
			mess.setMittente(utenteBean.getUtente());
			mess.setDestinatario(thread.getDestinatarioPrimo());
			mess.setOggetto(messaggio.getOggetto());
			mess.setThread(thr);
			Date ora = new Date();
			mess.setData(ora);
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
			contentBean.setContent("messaggi.xhtml");
			contentBean.setMessaggio("Thread nuovo");
		} else {
			System.out.println("INVIA MESSAGGIO==thread esistente");
			if (threadEsistente.isCancellatoThreadMittente()) {
				getContentBean().setMessaggio("Thread già esistente: messaggio aggiunto!");
				threadEsistente.setCancellatoThreadMittente(false);
			}
			Messaggio mess = new Messaggio();
			mess.setMessaggio(messaggio.getMessaggio());
			mess.setMittente(utenteBean.getUtente());
			mess.setDestinatario(threadEsistente.getDestinatarioPrimo());
			mess.setOggetto(messaggio.getOggetto());
			mess.setThread(threadEsistente);
			Date ora = new Date();
			mess.setData(ora);
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
			contentBean.setContent("messaggi.xhtml");
//			contentBean.setMessaggio("Thread esistente");
		}
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
	}
	
	
	public void cercaThreadsRicevutiUtente (int idUtente) {

		EntityManager em = EMF.createEntityManager();
		threadsRicevutiUtente = em
		.createQuery("from Thread t where t.destinatarioPrimo.id =:mitt and t.cancellatoThreadDestinatario = false")
		.setParameter("mitt", idUtente)
		.getResultList();	
	}
	
	
	public void visualizzaThread (int idMess) {
		EntityManager em = EMF.createEntityManager();
		List<Thread> threads = em
		.createQuery("from Thread t where t.id =:idThr")
		.setParameter("idThr", idMess)
		.getResultList();	
		if(threads!=null && threads.size()>0) {
			
//			for (Thread thread : threads) {
//				idMessaggiSelezionati.put(thread.getId(), false);
//				System.out.println("TH:"+thread.getId());
//			}		
			
			setThreadMessaggi(threads.get(0));	
			if (!threadMessaggi.isNuovoMessaggio()) {
				for (Messaggio m : threadMessaggi.getMessaggi()) {
//					if (!m.isLetto()) {
						m.setLetto(true);
//					}
				}
				nuoviMessaggiThread(utenteBean.getUtente().getId());

			}
			threadMessaggi.setNuovoMessaggio(false);
			nuoviMessaggiThread(utenteBean.getUtente().getId());
			try {
				serv.merge(threadMessaggi);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nuoviMessaggiThread(utenteBean.getUtente().getId());
//			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("menuutenteform");
			contentBean.setContent("messaggioThread.xhtml");
		}
		
	}
	
	
	public void nuoviMessaggiThread (int idUtente) {
		System.out.println("nuoviMESSAGGIingresso");
		EntityManager em = EMF.createEntityManager();
		List<Thread> threadsRicevutiUtente = em
		.createQuery("from Thread t where t.destinatarioPrimo.id =:mitt and t.nuovoMessaggio = true")
		.setParameter("mitt", idUtente)
		.getResultList();	
		System.out.println("nuoviMESSAGGItrovati"+threadsRicevutiUtente.size());
		nuoviMessaggi = threadsRicevutiUtente.size();
	}

	
	private Map<Integer, Boolean> idThreadsSelezionati = new HashMap<Integer, Boolean>();
	
	
//	public void cancellaThreadsSelezionati (List<Thread> thrSel) {
//		for (Thread t : thrSel)  {
//			if (idThreadsSelezionati.get(t.getId()).booleanValue()) {
//				try {
//					serv.delete(t);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		cercaThreadsInviatiUtente(utenteBean.getUtente().getId());
//		cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
//	}
	
	
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
		cercaThreadsRicevutiUtente(utenteBean.getUtente().getId());
	}
	
	//************GETTERS & SETTERS*******************
	
	
	
	@ManagedProperty(value = "#{utenteBean}")
	private UtenteBean utenteBean;	

	@ManagedProperty(value = "#{contentBean}")
	private ContentBean contentBean;

	


	public Map<Integer, Boolean> getIdThreadsSelezionati() {
		return idThreadsSelezionati;
	}

	public void setIdThreadsSelezionati(Map<Integer, Boolean> idThreadsSelezionati) {
		this.idThreadsSelezionati = idThreadsSelezionati;
	}

	public int getNuoviMessaggi() {
		System.out.println("ggETNuOVIMESSAgGI"+nuoviMessaggi);
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