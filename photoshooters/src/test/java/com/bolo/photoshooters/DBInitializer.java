package com.bolo.photoshooters;

//import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;

import com.bolo.photo.web.entity.Album;
import com.bolo.photo.web.entity.Foto;
import com.bolo.photo.web.entity.Membership;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoLavoro;
import com.bolo.photo.web.entity.TipoMembership;
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
	}
	
//	public static java.sql.Date convertFromJAVADateToSQLDate(java.util.Date javaDate) {
//        java.sql.Date sqlDate = null;
//        if (javaDate != null) {
//            sqlDate = new Date(javaDate.getTime());
//        }
//        return sqlDate;
//    }
	
	public void initTipiUtentiLavori(EntityManager em) throws Exception{
		
//		tipologie merbership
		TipoMembership free = new TipoMembership();
		TipoMembership plus = new TipoMembership();
		free.setId(1);
		free.setDescrizione("FREe");
		plus.setId(2);
		plus.setDescrizione("PLUs");
		
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
		grafico.setDescrizione("GRAFICo/a-POSt PRODUCEr");
		
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
		hairmodel.getTipiUtente().add(modello);
		hostess.getTipiLavoro().add(hairmodel);
		hairmodel.getTipiUtente().add(hostess);
		
		TipoLavoro ritratto = new TipoLavoro();
		ritratto.setId(2);
		ritratto.setDescrizione("RITRATTo");
		modello.getTipiLavoro().add(ritratto);	
		ritratto.getTipiUtente().add(modello);
		fotografo.getTipiLavoro().add(ritratto);
		ritratto.getTipiUtente().add(fotografo);
		
		TipoLavoro fashion = new TipoLavoro();
		fashion.setId(3);
		fashion.setDescrizione("FASHIOn");
		modello.getTipiLavoro().add(fashion);
		fashion.getTipiUtente().add(modello);
		fotografo.getTipiLavoro().add(fashion);
		fashion.getTipiUtente().add(fotografo);
		mua.getTipiLavoro().add(fashion);
		fashion.getTipiUtente().add(mua);
		costumista.getTipiLavoro().add(fashion);
		fashion.getTipiUtente().add(costumista);
		stilista.getTipiLavoro().add(fashion);
		fashion.getTipiUtente().add(stilista);
		
		TipoLavoro modamare = new TipoLavoro();
		modamare.setId(4);
		modamare.setDescrizione("MODa MARe");
		modello.getTipiLavoro().add(modamare);
		modamare.getTipiUtente().add(fotografo);
		
		TipoLavoro intimo = new TipoLavoro();
		intimo.setId(5);
		intimo.setDescrizione("INTIMo");
		modello.getTipiLavoro().add(intimo);
		intimo.getTipiUtente().add(fotografo);
		
		TipoLavoro glamour = new TipoLavoro();
		glamour.setId(6);
		glamour.setDescrizione("GLAMOUr-TRSPARENZe");
		modello.getTipiLavoro().add(glamour);
		glamour.getTipiUtente().add(modello);
		fotografo.getTipiLavoro().add(glamour);
		glamour.getTipiUtente().add(fotografo);
		
		TipoLavoro topless = new TipoLavoro();
		topless.setId(7);
		topless.setDescrizione("TOPLESs");
		modello.getTipiLavoro().add(topless);
		topless.getTipiUtente().add(modello);
		
		TipoLavoro nudo = new TipoLavoro();
		nudo.setId(8);
		nudo.setDescrizione("NUDo");
		modello.getTipiLavoro().add(nudo);
		nudo.getTipiUtente().add(modello);
		
		TipoLavoro artistico = new TipoLavoro();
		artistico.setId(9);
		artistico.setDescrizione("ARTISTICo");
		modello.getTipiLavoro().add(artistico);	
		artistico.getTipiUtente().add(modello);
		fotografo.getTipiLavoro().add(artistico);	
		artistico.getTipiUtente().add(fotografo);
		mua.getTipiLavoro().add(artistico);
		artistico.getTipiUtente().add(mua);
		costumista.getTipiLavoro().add(artistico);
		artistico.getTipiUtente().add(costumista);
		stilista.getTipiLavoro().add(artistico);
		artistico.getTipiUtente().add(stilista);
		grafico.getTipiLavoro().add(artistico);
		artistico.getTipiUtente().add(grafico);
		
		
		TipoLavoro street = new TipoLavoro();
		street.setId(10);
		street.setDescrizione("STREEt");
		fotografo.getTipiLavoro().add(street);
		street.getTipiUtente().add(fotografo);
	
		TipoLavoro natura = new TipoLavoro();
		natura.setId(11);
		natura.setDescrizione("NATURa");
		fotografo.getTipiLavoro().add(natura);
		natura.getTipiUtente().add(fotografo);
		
		TipoLavoro sport = new TipoLavoro();
		sport.setId(12);
		sport.setDescrizione("SPORt");
		fotografo.getTipiLavoro().add(sport);
		sport.getTipiUtente().add(fotografo);
		
		TipoLavoro reportage = new TipoLavoro();
		reportage.setId(13);
		reportage.setDescrizione("REPORTAGe");
		fotografo.getTipiLavoro().add(reportage);
		reportage.getTipiUtente().add(fotografo);
		
		TipoLavoro sposa = new TipoLavoro();
		sposa.setId(14);
		sposa.setDescrizione("SPOSa");
		mua.getTipiLavoro().add(sposa);
		sposa.getTipiUtente().add(mua);
		costumista.getTipiLavoro().add(sposa);
		sposa.getTipiUtente().add(costumista);
		stilista.getTipiLavoro().add(sposa);
		sposa.getTipiUtente().add(stilista);
		
		TipoLavoro tvcinema = new TipoLavoro();
		tvcinema.setId(15);
		tvcinema.setDescrizione("Tv-CINEMa");
		mua.getTipiLavoro().add(tvcinema);
		tvcinema.getTipiUtente().add(mua);
		costumista.getTipiLavoro().add(tvcinema);
		tvcinema.getTipiUtente().add(costumista);
		stilista.getTipiLavoro().add(tvcinema);
		tvcinema.getTipiUtente().add(stilista);
		
		TipoLavoro eventi = new TipoLavoro();
		eventi.setId(16);
		eventi.setDescrizione("EVENTi");
		mua.getTipiLavoro().add(eventi);
		eventi.getTipiUtente().add(mua);
		costumista.getTipiLavoro().add(eventi);
		eventi.getTipiUtente().add(costumista);
		stilista.getTipiLavoro().add(eventi);
		eventi.getTipiUtente().add(stilista);
		grafico.getTipiLavoro().add(eventi);
		eventi.getTipiUtente().add(grafico);
		hostess.getTipiLavoro().add(eventi);
		eventi.getTipiUtente().add(hostess);
		
		TipoLavoro bodypainting = new TipoLavoro();
		bodypainting.setId(17);
		bodypainting.setDescrizione("BODy PAINTINg");
		mua.getTipiLavoro().add(bodypainting);
		bodypainting.getTipiUtente().add(mua);
	
		TipoLavoro postproduzione = new TipoLavoro();
		postproduzione.setId(18);
		postproduzione.setDescrizione("POSt PRODUZIONe FOTo/VIDEo");
		grafico.getTipiLavoro().add(postproduzione);
		postproduzione.getTipiUtente().add(grafico);
		
		TipoLavoro pubblicita = new TipoLavoro();
		pubblicita.setId(19);
		pubblicita.setDescrizione("PUBBLICITà");
		grafico.getTipiLavoro().add(pubblicita);
		pubblicita.getTipiUtente().add(grafico);
		hostess.getTipiLavoro().add(pubblicita);
		pubblicita.getTipiUtente().add(hostess);
		agenzia.getTipiLavoro().add(pubblicita);
		pubblicita.getTipiUtente().add(agenzia);
		
		TipoLavoro modasfilate = new TipoLavoro();
		modasfilate.setId(20);
		modasfilate.setDescrizione("MODa-SFILATe");
		grafico.getTipiLavoro().add(modasfilate);
		modasfilate.getTipiUtente().add(grafico);
		stilista.getTipiLavoro().add(modasfilate);
		modasfilate.getTipiUtente().add(stilista);
		agenzia.getTipiLavoro().add(modasfilate);
		modasfilate.getTipiUtente().add(agenzia);
		
		TipoLavoro servizifotografici = new TipoLavoro();
		servizifotografici.setId(21);
		servizifotografici.setDescrizione("SERVIZi FOTOGRAFICi");
		agenzia.getTipiLavoro().add(servizifotografici);
		servizifotografici.getTipiUtente().add(agenzia);
		mua.getTipiLavoro().add(servizifotografici);
		servizifotografici.getTipiUtente().add(mua);
		studiofotografico.getTipiLavoro().add(servizifotografici);
		servizifotografici.getTipiUtente().add(studiofotografico);
		
		TipoLavoro salaposeattrezzatura = new TipoLavoro();
		salaposeattrezzatura.setId(22);
		salaposeattrezzatura.setDescrizione("ATTREZZATURa-ASSISTENZa");
		studiofotografico.getTipiLavoro().add(salaposeattrezzatura);
		salaposeattrezzatura.getTipiUtente().add(studiofotografico);
		
		TipoLavoro setpersonalizzati = new TipoLavoro();
		setpersonalizzati.setId(23);
		setpersonalizzati.setDescrizione("SEt PERSONALIZZATi");
		studiofotografico.getTipiLavoro().add(setpersonalizzati);
		setpersonalizzati.getTipiUtente().add(studiofotografico);
		
		TipoLavoro concorsi = new TipoLavoro();
		concorsi.setId(24);
		concorsi.setDescrizione("CONCORSi");
		agenzia.getTipiLavoro().add(concorsi);
		concorsi.getTipiUtente().add(agenzia);
		
		TipoLavoro mostre = new TipoLavoro();
		mostre.setId(25);
		mostre.setDescrizione("MOSTRe");
		agenzia.getTipiLavoro().add(mostre);
		mostre.getTipiUtente().add(agenzia);
		hostess.getTipiLavoro().add(mostre);
		mostre.getTipiUtente().add(hostess);
		
		TipoLavoro standfiere = new TipoLavoro();
		standfiere.setId(26);
		standfiere.setDescrizione("HOSTESs-STANd PEr FIERe");
		agenzia.getTipiLavoro().add(standfiere);
		standfiere.getTipiUtente().add(agenzia);
		hostess.getTipiLavoro().add(standfiere);
		standfiere.getTipiUtente().add(hostess);
		
		TipoLavoro fotomodella = new TipoLavoro();
		fotomodella.setId(27);
		fotomodella.setDescrizione("FOTOMODELLa");
		hostess.getTipiLavoro().add(fotomodella);
		fotomodella.getTipiUtente().add(hostess);
		
		
//		utenti test
		Utente test = new Utente();
		test.setId(1);
		test.setName("fabio");
		test.setUsername("fb");
		test.setPassword("fb");
		test.setEmail("portoricano2000@gmail.com");
		test.setTipoUtente(fotografo);
		test.setActive(true);
		test.setAvatar("avatarDefault.svg");
		DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String strdate1 = "02-04-2011 11:35:42";
		java.util.Date dataAccesso1;
		dataAccesso1 = dateformat.parse(strdate1);
		test.setDataUltimoAccesso(dataAccesso1);
		String strIscr1 = "12-04-2012 21:35:42";
		java.util.Date dataIscr1;
		dataIscr1 = dateformat.parse(strIscr1);
		test.setDataIscrizione(dataIscr1);
		test.setSesso(Sesso.Donna);
		Album album1 = new Album(); 

		List<Album> albumlist1 = new ArrayList<Album>();
		test.setPubblicati(albumlist1);

		test.getPubblicati().add(album1);
		album1.setPubblicatore(test);
		album1.setTitolo("AlbumFB1");
		Foto foto1 = new Foto();
		foto1.setAlbum(album1);
		album1.getFotos().add(foto1);
		foto1.setPubblicatore(test);
		foto1.setTitolo("FotoFB1");
		foto1.setAltezzaFoto(600);
		foto1.setLarghezzaFoto(800);
		foto1.setNomeFileFoto("nomeFotoFB1.jpg");
		
		Membership testMShip = new Membership();
		testMShip.setTipoMembership(plus);
		Date dataInizio = new Date();
		String strdataInizio = "02-04-2013 00:35:42";
		dataInizio = dateformat.parse(strdataInizio);
		testMShip.setDataInizio(dataInizio);
		Date dataFine = new Date();
		String strdataFine = "02-04-2017 00:35:42";
		dataFine = dateformat.parse(strdataFine);
		testMShip.setDataFine(dataFine);
		testMShip.setUtente(test);
		List<Membership> listaMShip = new ArrayList<Membership>();
		listaMShip.add(testMShip);
		test.setMemberships(listaMShip);
		
		
		
		Utente test2 = new Utente();
		test2.setId(2);
		test2.setName("fabio2");
		test2.setUsername("fb2");
		test2.setPassword("fb2");
		test2.setEmail("fbolo@inwind.it");
		test2.setTipoUtente(modello);
		test2.setActive(true);
		test2.setAvatar("avatarDefault.svg");
		String strdate2 = "02-06-2014 18:12:32";
		java.util.Date dataAccesso2;
		dataAccesso2 = dateformat.parse(strdate2);
		test2.setDataUltimoAccesso(dataAccesso2);
		String strIscr2 = "12-06-2014 21:35:42";
		java.util.Date dataIscr2;
		dataIscr2 = dateformat.parse(strIscr2);
		test2.setDataIscrizione(dataIscr2);
		test2.setSesso(Sesso.Uomo);
		Album album2 = new Album(); 

		List<Album> albumlist2 = new ArrayList<Album>();
		test2.setPubblicati(albumlist2);

		test2.getPubblicati().add(album2);
		album2.setPubblicatore(test2);
		album2.setTitolo("fabiofb2");
		
		Membership test2MShip = new Membership();
		test2MShip.setTipoMembership(free);
		Date dataInizio2 = new Date();
		String strdataInizio2 = "02-04-2013 00:35:42";
		dataInizio2 = dateformat.parse(strdataInizio2);
		test2MShip.setDataInizio(dataInizio2);
		Date dataFine2 = new Date();
		String strdataFine2 = "02-04-2017 00:35:42";
		dataFine = dateformat.parse(strdataFine2);
		test2MShip.setDataFine(dataFine2);
		test2MShip.setUtente(test2);
		List<Membership> listaMShip2 = new ArrayList<Membership>();
		listaMShip2.add(test2MShip);
		test.setMemberships(listaMShip2);
		
		
		Utente test3 = new Utente();
		test3.setId(3);
		test3.setName("fabio3");
		test3.setUsername("fb3");
		test3.setPassword("fb3");
		test3.setEmail("fbolo3@inwind.it");
		test3.setTipoUtente(hostess);
		test3.setActive(true);
		test3.setAvatar("avatarDefault.svg");
		String strdate3 = "02-06-2013 12:12:32";
		java.util.Date dataAccesso3;
		dataAccesso3 = dateformat.parse(strdate3);
		test3.setDataUltimoAccesso(dataAccesso3);
		String strIscr3 = "12-10-2013 21:35:42";
		java.util.Date dataIscr3;
		dataIscr3 = dateformat.parse(strIscr3);
		test3.setDataIscrizione(dataIscr3);
		test3.setSesso(Sesso.Uomo);
		
		Membership test3MShip = new Membership();
		test3MShip.setTipoMembership(plus);
		Date dataInizio3 = new Date();
		String strdataInizio3 = "03-04-2013 00:35:43";
		dataInizio3 = dateformat.parse(strdataInizio3);
		test3MShip.setDataInizio(dataInizio3);
		Date dataFine3 = new Date();
		String strdataFine3 = "03-04-2017 00:35:43";
		dataFine = dateformat.parse(strdataFine3);
		test3MShip.setDataFine(dataFine3);
		test3MShip.setUtente(test3);
		List<Membership> listaMShip3 = new ArrayList<Membership>();
		listaMShip3.add(test3MShip);
		test.setMemberships(listaMShip3);
		
		
		Utente test4 = new Utente();
		test4.setId(4);
		test4.setName("fabio");
		test4.setUsername("fb4");
		test4.setPassword("fb4");
		test4.setEmail("fbolo4@inwind.it");
		test4.setTipoUtente(mua);
		test4.setActive(true);
		test4.setAvatar("avatarDefault.svg");
		String strdate4 = "01-06-2012 10:12:32";
		java.util.Date dataAccesso4;
		dataAccesso4 = dateformat.parse(strdate4);
		test4.setDataUltimoAccesso(dataAccesso4);
		String strIscr4 = "12-11-2011 21:35:42";
		java.util.Date dataIscr4;
		dataIscr4 = dateformat.parse(strIscr4);
		test4.setDataIscrizione(dataIscr4);
		test4.setSesso(Sesso.Società);
		
		Membership test4MShip = new Membership();
		test4MShip.setTipoMembership(free);
		Date dataInizio4 = new Date();
		String strdataInizio4 = "04-04-2014 00:45:44";
		dataInizio4 = dateformat.parse(strdataInizio4);
		test4MShip.setDataInizio(dataInizio4);
		Date dataFine4 = new Date();
		String strdataFine4 = "04-04-2017 00:45:44";
		dataFine = dateformat.parse(strdataFine4);
		test4MShip.setDataFine(dataFine4);
		test4MShip.setUtente(test4);
		List<Membership> listaMShip4 = new ArrayList<Membership>();
		listaMShip4.add(test4MShip);
		test.setMemberships(listaMShip4);
		
		
		serv.deleteAll(Album.class, em);
		serv.deleteAll(Foto.class, em);
		serv.deleteAll(Utente.class, em);
		serv.deleteAll(TipoLavoro.class, em);
		serv.deleteAll(TipoUtente.class, em);		
		serv.deleteAll(Membership.class, em);
		serv.deleteAll(TipoMembership.class, em);
		
		serv.persist(test, em);	
		serv.persist(test2, em);
		serv.persist(test3, em);
		serv.persist(test4, em);
//		serv.persist(album1, em);
//		serv.persist(foto1, em);
		
		serv.persist(free, em);
		serv.persist(plus, em);
		serv.persist(testMShip, em);
		serv.persist(test2MShip, em);
		serv.persist(test3MShip, em);
		serv.persist(test4MShip, em);
		
		serv.persist(hairmodel, em);
		serv.persist(ritratto, em);
		serv.persist(fashion, em);
		serv.persist(modamare, em);
		serv.persist(intimo, em);
		serv.persist(glamour, em);
		serv.persist(topless, em);
		serv.persist(nudo, em);
		serv.persist(artistico, em);
		serv.persist(street, em);
		serv.persist(natura, em);
		serv.persist(sport, em);
		serv.persist(reportage, em);
		serv.persist(sposa, em);
		serv.persist(tvcinema, em);
		serv.persist(eventi, em);
		serv.persist(bodypainting, em);
		serv.persist(postproduzione, em);
		serv.persist(pubblicita, em);
		serv.persist(modasfilate, em);
		serv.persist(servizifotografici, em);
		serv.persist(salaposeattrezzatura, em);
		serv.persist(setpersonalizzati, em);
		serv.persist(concorsi, em);
		serv.persist(mostre, em);
		serv.persist(standfiere, em);
		serv.persist(fotomodella, em);
		
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
