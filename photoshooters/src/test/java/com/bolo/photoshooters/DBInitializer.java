package com.bolo.photoshooters;

import javax.persistence.EntityManager;

import com.bolo.photo.web.entity.TipoLavoro;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.web.EMF;

public class DBInitializer {
	
	private ServiziComuni serv;
	
	public DBInitializer(){
		serv = new ServiziComuniImpl();
	}

	public static void main(String[] args) {
		System.setProperty("test.dal.mio.pc", "OK");
		DBInitializer dbInit = new DBInitializer();
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
				
		try {
			
			dbInit.clean(em);
			dbInit.initTipiUtentiLavori(em);
				
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		em.close();
	}
	
		
	public void clean(EntityManager em) throws Exception{
		serv.deleteAll(Utente.class, em);
//		Utente test = new Utente();
//		test.setId(1);
//		test.setName("fabio");
//		test.setUsername("fb");
//		test.setPassword("fb");
//		test.setEmail("portoricano2000@gmail.com");
//		test.tipoUtente.setId(1);
	}

	public void initTipiUtentiLavori(EntityManager em) throws Exception{
		TipoUtente fotografo = new TipoUtente();
		fotografo.setId(1);
		fotografo.setDescrizione("FOTOGRAFo/a");
		
		TipoUtente modello = new TipoUtente();
		modello.setId(2);
		modello.setDescrizione("MODELLo/a");
	
		TipoUtente mua = new TipoUtente();
		mua.setId(3);
		mua.setDescrizione("MUa-HAIRSTYLISt");
		
		TipoUtente costumista = new TipoUtente();
		costumista.setId(4);
		costumista.setDescrizione("COSTUMISTa");
		
		TipoUtente stilista = new TipoUtente();
		stilista.setId(5);
		stilista.setDescrizione("STILISTa");
		
		TipoUtente grafico = new TipoUtente();
		grafico.setId(6);
		grafico.setDescrizione("GRAFICo-POSt PRODUCEr");
		
		TipoUtente studiofotografico = new TipoUtente();
		studiofotografico.setId(7);
		studiofotografico.setDescrizione("STUDIo FOTOGRAFICo");
		
		TipoUtente agenzia = new TipoUtente();
		agenzia.setId(8);
		agenzia.setDescrizione("AGENZIa");
		
		TipoUtente hostess = new TipoUtente();
		hostess.setId(9);
		hostess.setDescrizione("HOSTESs");
		
		TipoLavoro hairmodel = new TipoLavoro();
		hairmodel.setId(1);
		hairmodel.setDescrizione("HAIRMODEl");
		modello.getTipiLavoro().add(hairmodel);
		
		TipoLavoro ritratto = new TipoLavoro();
		ritratto.setId(2);
		ritratto.setDescrizione("RITRATTo");
		modello.getTipiLavoro().add(ritratto);	
	
		TipoLavoro fashion = new TipoLavoro();
		fashion.setId(3);
		fashion.setDescrizione("FASHIOn");
		modello.getTipiLavoro().add(fashion);
		
		TipoLavoro modamare = new TipoLavoro();
		modamare.setId(4);
		modamare.setDescrizione("MODa MARe");
		modello.getTipiLavoro().add(modamare);	
		
		TipoLavoro intimo = new TipoLavoro();
		intimo.setId(5);
		intimo.setDescrizione("INTIMo");
		modello.getTipiLavoro().add(intimo);
		
		TipoLavoro glamour = new TipoLavoro();
		glamour.setId(6);
		glamour.setDescrizione("GLAMOUr-TRSPARENZe");
		modello.getTipiLavoro().add(glamour);	
		
		TipoLavoro topless = new TipoLavoro();
		topless.setId(7);
		topless.setDescrizione("TOPLESs");
		modello.getTipiLavoro().add(topless);	
		
		TipoLavoro nudo = new TipoLavoro();
		nudo.setId(8);
		nudo.setDescrizione("NUDo");
		modello.getTipiLavoro().add(nudo);
		
		TipoLavoro art = new TipoLavoro();
		art.setId(9);
		art.setDescrizione("ARt");
		modello.getTipiLavoro().add(art);	
		
		serv.deleteAll(TipoUtente.class, em);		
		serv.deleteAll(TipoLavoro.class, em);
		
		serv.persist(hairmodel, em);
		serv.persist(ritratto, em);
		serv.persist(fashion, em);
		serv.persist(modamare, em);
		serv.persist(intimo, em);
		serv.persist(glamour, em);
		serv.persist(topless, em);
		serv.persist(nudo, em);
		serv.persist(art, em);

		serv.persist(fotografo, em);
		serv.persist(modello, em);
		serv.persist(mua, em);
		serv.persist(costumista, em);
		serv.persist(stilista, em);
		serv.persist(grafico, em);
		serv.persist(studiofotografico, em);
		serv.persist(agenzia, em);
		serv.persist(hostess, em);
	}

}
