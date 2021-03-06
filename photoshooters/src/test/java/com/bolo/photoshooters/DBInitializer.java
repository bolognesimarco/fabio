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
import com.bolo.photo.web.entity.Annuncio;
import com.bolo.photo.web.entity.Foto;
import com.bolo.photo.web.entity.Membership;
import com.bolo.photo.web.entity.Messaggio;
import com.bolo.photo.web.entity.Post;
import com.bolo.photo.web.entity.SuperPost;
import com.bolo.photo.web.entity.Thread;
import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoLavoro;
import com.bolo.photo.web.entity.TipoMembership;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photo.web.entity.TopicForum;
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

		
//		LAVORI------------------------------------------------------------------------------------------------
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
		pubblicita.setDescrizione("PUBBLICIT�");
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
		
		
//		UTENTI test---------------------------------------------------------------------
		Utente test = new Utente();
		test.setId(2);
		test.setName("fabio");
		test.setUsername("fb");
		test.setPassword("780cd58b236bb2dcbc5b9c55768c7554701ecc10daefb9db0db3355a37ab3e317f77436eafb719b02bcb25385eeeed8cea5371a3ff4f81852d614eef50bcf7f6");
		test.setSalt("[B@bd69367");
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
		test2.setId(3);
		test2.setName("fabio2");
		test2.setUsername("fb2");
		test2.setPassword("780cd58b236bb2dcbc5b9c55768c7554701ecc10daefb9db0db3355a37ab3e317f77436eafb719b02bcb25385eeeed8cea5371a3ff4f81852d614eef50bcf7f6");
		test2.setSalt("[B@bd69367");
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
		dataFine2 = dateformat.parse(strdataFine2);
		test2MShip.setDataFine(dataFine2);
		test2MShip.setUtente(test2);
		List<Membership> listaMShip2 = new ArrayList<Membership>();
		listaMShip2.add(test2MShip);
		test2.setMemberships(listaMShip2);
		
		
		Utente test3 = new Utente();
		test3.setId(4);
		test3.setName("fabio3");
		test3.setUsername("fb3");
		test3.setPassword("780cd58b236bb2dcbc5b9c55768c7554701ecc10daefb9db0db3355a37ab3e317f77436eafb719b02bcb25385eeeed8cea5371a3ff4f81852d614eef50bcf7f6");
		test3.setSalt("[B@bd69367");
		test3.setEmail("fbolo3----@inwind.it");
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
		dataFine3 = dateformat.parse(strdataFine3);
		test3MShip.setDataFine(dataFine3);
		test3MShip.setUtente(test3);
		List<Membership> listaMShip3 = new ArrayList<Membership>();
		listaMShip3.add(test3MShip);
		test3.setMemberships(listaMShip3);
		
		
		Utente test4 = new Utente();
		test4.setId(5);
		test4.setName("fabio");
		test4.setUsername("fb4");
		test4.setPassword("780cd58b236bb2dcbc5b9c55768c7554701ecc10daefb9db0db3355a37ab3e317f77436eafb719b02bcb25385eeeed8cea5371a3ff4f81852d614eef50bcf7f6");
		test4.setSalt("[B@bd69367");
		test4.setEmail("fbolo4-----@inwind.it");
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
		test4.setSesso(Sesso.Societ�);
		test4.setOnline(true);
		
		Membership test4MShip = new Membership();
		test4MShip.setTipoMembership(free);
		Date dataInizio4 = new Date();
		String strdataInizio4 = "04-04-2014 00:45:44";
		dataInizio4 = dateformat.parse(strdataInizio4);
		test4MShip.setDataInizio(dataInizio4);
		Date dataFine4 = new Date();
		String strdataFine4 = "04-04-2017 00:45:44";
		dataFine4 = dateformat.parse(strdataFine4);
		test4MShip.setDataFine(dataFine4);
		test4MShip.setUtente(test4);
		List<Membership> listaMShip4 = new ArrayList<Membership>();
		listaMShip4.add(test4MShip);
		test4.setMemberships(listaMShip4);
		
		
		Utente admin = new Utente();
		admin.setId(1);
		admin.setName("admin");
		admin.setUsername("ADMIn");
		admin.setPassword("780cd58b236bb2dcbc5b9c55768c7554701ecc10daefb9db0db3355a37ab3e317f77436eafb719b02bcb25385eeeed8cea5371a3ff4f81852d614eef50bcf7f6");
		admin.setSalt("[B@bd69367");
		admin.setEmail("photoshooters.net@gmail.com");
		admin.setTipoUtente(agenzia);
		admin.setActive(true);
		admin.setAvatar("avatarDefault.svg");
		String strdate5 = "01-06-1999 10:12:32";
		java.util.Date dataAccesso5;
		dataAccesso5 = dateformat.parse(strdate5);
		admin.setDataUltimoAccesso(dataAccesso5);
		String strIscr5 = "12-11-2011 21:35:42";
		java.util.Date dataIscr5;
		dataIscr5 = dateformat.parse(strIscr5);
		admin.setDataIscrizione(dataIscr5);
		admin.setSesso(Sesso.Societ�);
		admin.setOnline(false);
		
		Membership adminMShip = new Membership();
		adminMShip.setTipoMembership(plus);
		Date dataInizio5 = new Date();
		String strdataInizio5 = "04-04-2000 00:45:44";
		dataInizio5 = dateformat.parse(strdataInizio5);
		adminMShip.setDataInizio(dataInizio5);
		Date dataFine5 = new Date();
		String strdataFine5 = "04-04-2150 00:45:44";
		dataFine5 = dateformat.parse(strdataFine5);
		adminMShip.setDataFine(dataFine5);
		adminMShip.setUtente(admin);
		List<Membership> listaMShip5 = new ArrayList<Membership>();
		listaMShip5.add(adminMShip);
		admin.setMemberships(listaMShip5);
		
		
		
		
//	POST & SUPERPOST*********************************************************	
		Post regolamentoForumPost = new Post();
		regolamentoForumPost.setId(1);
		regolamentoForumPost.setProponente(admin);
		admin.getPostsPartecipati().add(regolamentoForumPost);
		regolamentoForumPost.getPartecipanti().add(admin);
		Thread regolamentoForumThread = new Thread();
		regolamentoForumPost.getRisposte().add(regolamentoForumThread);
		regolamentoForumThread.setOggettoThread("REGOLAMENTo FORUm");
		regolamentoForumThread.setMittentePrimo(admin);
		regolamentoForumThread.setDestinatarioPrimo(admin);
		regolamentoForumThread.setPost(regolamentoForumPost);
		Messaggio messRegolamento = new Messaggio();
		messRegolamento.getLetto().add(admin);
		regolamentoForumThread.getMessaggi().add(messRegolamento);
		messRegolamento.setThread(regolamentoForumThread);
		Date dataRegolamento = new Date();
		String strdataRegolamento = "01-01-1999 00:00:00";
		dataRegolamento = dateformat.parse(strdataRegolamento);
		messRegolamento.setData(dataRegolamento);
		messRegolamento.setOggetto(regolamentoForumThread.getOggettoThread());
		messRegolamento.setMittente(admin);
		messRegolamento.setDestinatario(admin);
		messRegolamento.setMessaggio("Regolamento del forum aggiornato.");
		
		Post regolamentoSitoPost = new Post();
		regolamentoSitoPost.setId(11);
		regolamentoSitoPost.setProponente(admin);
		admin.getPostsPartecipati().add(regolamentoSitoPost);
		regolamentoSitoPost.getPartecipanti().add(admin);
		Thread regolamentoThread = new Thread();
		regolamentoSitoPost.getRisposte().add(regolamentoThread);
		regolamentoThread.setOggettoThread("REGOLAMENTo SITo");
		regolamentoThread.setMittentePrimo(admin);
		regolamentoThread.setDestinatarioPrimo(admin);
		regolamentoThread.setPost(regolamentoSitoPost);
		Messaggio messRegolamento1 = new Messaggio();
		messRegolamento1.getLetto().add(admin);
		regolamentoThread.getMessaggi().add(messRegolamento1);
		messRegolamento1.setThread(regolamentoThread);
		Date dataRegolamento1 = new Date();
		String strdataRegolamento1 = "01-01-1999 00:00:00";
		dataRegolamento1 = dateformat.parse(strdataRegolamento1);
		messRegolamento1.setData(dataRegolamento1);
		messRegolamento1.setOggetto(regolamentoThread.getOggettoThread());
		messRegolamento1.setMittente(admin);
		messRegolamento1.setDestinatario(admin);
		messRegolamento1.setMessaggio("Regolamento del sito aggiornato.");
		
		SuperPost regolamentoForumSP = new SuperPost();
		regolamentoForumSP.setNomeSuperPost("REGOLAMENTo FORUm");
		regolamentoForumSP.setId(81);		
		regolamentoForumPost.setSuperpost(regolamentoForumSP);
		regolamentoForumSP.getPosts().add(regolamentoForumPost);
		
		SuperPost regolamentoSitoSP = new SuperPost();
		regolamentoSitoSP.setNomeSuperPost("REGOLAMENTo SITo");
		regolamentoSitoSP.setId(101);
		regolamentoSitoPost.setSuperpost(regolamentoSitoSP);
		regolamentoSitoSP.getPosts().add(regolamentoSitoPost);
		
		SuperPost presentazioneUtentiFreeSP = new SuperPost();
		presentazioneUtentiFreeSP.setNomeSuperPost("PRESENTAZIONe - UTENTi FREe");
		presentazioneUtentiFreeSP.setId(1);
		
		Post presentazioneUtentiFreePost0 = new Post();
		presentazioneUtentiFreePost0.setSuperpost(presentazioneUtentiFreeSP);
		presentazioneUtentiFreeSP.getPosts().add(presentazioneUtentiFreePost0);
		presentazioneUtentiFreePost0.setId(2);
		presentazioneUtentiFreePost0.setProponente(admin);
		admin.getPostsPartecipati().add(presentazioneUtentiFreePost0);
		presentazioneUtentiFreePost0.getPartecipanti().add(admin);
		presentazioneUtentiFreePost0.setPostUtentiFree(true);
		Thread presentatiThread0 = new Thread();
//		presentatiThread0.setNuovoMessaggio(true);
		presentazioneUtentiFreePost0.getRisposte().add(presentatiThread0);
		presentatiThread0.setOggettoThread("PRESENTAZIONe - UTENTi FREe");
		presentatiThread0.setMittentePrimo(admin);
		presentatiThread0.setDestinatarioPrimo(admin);
		presentatiThread0.setPost(presentazioneUtentiFreePost0);
		Messaggio messPresentati0 = new Messaggio();
		messPresentati0.getLetto().add(admin);
		presentatiThread0.getMessaggi().add(messPresentati0);
		messPresentati0.setThread(presentatiThread0);
		messPresentati0.setMessaggio("Sezione dedicata alla presentazione degli utenti free.");
		Date dataPresentati0 = new Date();
		String strdataPresentati0 = "01-01-1999 00:00:00";
		dataPresentati0 = dateformat.parse(strdataPresentati0);
		messPresentati0.setData(dataPresentati0);
		messPresentati0.setOggetto(presentatiThread0.getOggettoThread());
		messPresentati0.setMittente(admin);
		messPresentati0.setDestinatario(admin);
		
		Post presentazioneUtentiFreePost1 = new Post();
		presentazioneUtentiFreePost1.setSuperpost(presentazioneUtentiFreeSP);
		presentazioneUtentiFreeSP.getPosts().add(presentazioneUtentiFreePost1);
		presentazioneUtentiFreePost1.setId(3);	
		presentazioneUtentiFreePost1.setProponente(test);
		test.getPostsPartecipati().add(presentazioneUtentiFreePost1);
		presentazioneUtentiFreePost1.getPartecipanti().add(test);
		Thread presentatiThread1 = new Thread();
		presentazioneUtentiFreePost1.getRisposte().add(presentatiThread1);
		presentatiThread1.setOggettoThread("Buongiorno");
		presentatiThread1.setMittentePrimo(test);
		presentatiThread1.setDestinatarioPrimo(test);
		presentatiThread1.setPost(presentazioneUtentiFreePost1);
		Messaggio messPresentati1 = new Messaggio();
		messPresentati1.getLetto().add(test);
		presentatiThread1.getMessaggi().add(messPresentati1);
		messPresentati1.setThread(presentatiThread1);
		messPresentati1.setMessaggio("ciao sono fb!!!");
		Date dataPresentati1 = new Date();
		String strdataPresentati1 = "04-04-2016 10:45:44";
		dataPresentati1 = dateformat.parse(strdataPresentati1);
		messPresentati1.setData(dataPresentati1);
		messPresentati1.setOggetto(presentatiThread1.getOggettoThread());
		messPresentati1.setMittente(test);
		messPresentati1.setDestinatario(test);
		
		
		Post presentazioneUtentiFreePost2 = new Post();
		presentazioneUtentiFreePost2.setSuperpost(presentazioneUtentiFreeSP);
		presentazioneUtentiFreeSP.getPosts().add(presentazioneUtentiFreePost2);
		presentazioneUtentiFreePost2.setId(4);	
		presentazioneUtentiFreePost2.setProponente(test2);
		test2.getPostsPartecipati().add(presentazioneUtentiFreePost2);
		presentazioneUtentiFreePost2.getPartecipanti().add(test2);
		Thread presentatiThread2 = new Thread();
		presentazioneUtentiFreePost2.getRisposte().add(presentatiThread2);
		presentatiThread2.setOggettoThread("CIAOOOOo 2");
		presentatiThread2.setMittentePrimo(test2);
		presentatiThread2.setDestinatarioPrimo(test2);
		presentatiThread2.setPost(presentazioneUtentiFreePost2);
		Messaggio messPresentati2 = new Messaggio();
		messPresentati2.getLetto().add(test2);
		presentatiThread2.getMessaggi().add(messPresentati2);
		messPresentati2.setThread(presentatiThread2);
		messPresentati2.setMessaggio("buongiorno sono fb2!!!");
		Date dataPresentati2 = new Date();
		String strdataPresentati2 = "14-05-2016 11:35:22";
		dataPresentati2 = dateformat.parse(strdataPresentati2);
		messPresentati2.setData(dataPresentati2);
		messPresentati2.setOggetto(presentatiThread2.getOggettoThread());
		messPresentati2.setMittente(test2);
		messPresentati2.setDestinatario(test2);
		
		
		SuperPost generaleUtentiFreeSP = new SuperPost();
		generaleUtentiFreeSP.setNomeSuperPost("GENERALe - UTENTi FREe");
		generaleUtentiFreeSP.setId(21);
		
		Post genericoUtentiFreePost0 = new Post();
		generaleUtentiFreeSP.getPosts().add(genericoUtentiFreePost0);
		genericoUtentiFreePost0.setSuperpost(generaleUtentiFreeSP);
		genericoUtentiFreePost0.setId(5);
		genericoUtentiFreePost0.setProponente(admin);
		admin.getPostsPartecipati().add(genericoUtentiFreePost0);
		genericoUtentiFreePost0.getPartecipanti().add(admin);
		genericoUtentiFreePost0.setPostUtentiFree(true);
		Thread genericoUtentiFreeThread0 = new Thread();
		genericoUtentiFreePost0.getRisposte().add(genericoUtentiFreeThread0);
		genericoUtentiFreeThread0.setOggettoThread("GENERALe - UTENTi FREe");
		genericoUtentiFreeThread0.setMittentePrimo(admin);
		genericoUtentiFreeThread0.setDestinatarioPrimo(admin);
		genericoUtentiFreeThread0.setPost(genericoUtentiFreePost0);
		Messaggio messGenericoUtentiFree0 = new Messaggio();
		messGenericoUtentiFree0.getLetto().add(admin);
		genericoUtentiFreeThread0.getMessaggi().add(messGenericoUtentiFree0);
		messGenericoUtentiFree0.setThread(genericoUtentiFreeThread0);
		messGenericoUtentiFree0.setMessaggio("Sezione generica per gli utenti free.");
		Date dataGenericoUtentiFree0 = new Date();
		String strdataGenericoUtentiFree0 = "01-01-1999 00:00:00";
		dataGenericoUtentiFree0 = dateformat.parse(strdataGenericoUtentiFree0);
		messGenericoUtentiFree0.setData(dataGenericoUtentiFree0);
		messGenericoUtentiFree0.setOggetto(genericoUtentiFreeThread0.getOggettoThread());
		messGenericoUtentiFree0.setMittente(admin);
		messGenericoUtentiFree0.setDestinatario(admin);
		
		Post genericoUtentiFreePost1 = new Post();
		generaleUtentiFreeSP.getPosts().add(genericoUtentiFreePost1);
		genericoUtentiFreePost1.setSuperpost(generaleUtentiFreeSP);
		genericoUtentiFreePost1.setId(6);
		genericoUtentiFreePost1.setProponente(test3);
		test3.getPostsPartecipati().add(genericoUtentiFreePost1);
		genericoUtentiFreePost1.getPartecipanti().add(test3);
		Thread genericoUtentiFreeThread1 = new Thread();
		genericoUtentiFreePost1.getRisposte().add(genericoUtentiFreeThread1);
		genericoUtentiFreeThread1.setOggettoThread("Buongiorno mondo-post generico fb3");
		genericoUtentiFreeThread1.setMittentePrimo(test3);
		genericoUtentiFreeThread1.setDestinatarioPrimo(test3);
		genericoUtentiFreeThread1.setPost(genericoUtentiFreePost1);
		Messaggio messGenericoUtentiFree1 = new Messaggio();
		messGenericoUtentiFree1.getLetto().add(test3);
		genericoUtentiFreeThread1.getMessaggi().add(messGenericoUtentiFree1);
		messGenericoUtentiFree1.setThread(genericoUtentiFreeThread1);
		messGenericoUtentiFree1.setMessaggio("Salut fb3 post generico(old)!!!");
		Date dataGenericoUtentiFree1 = new Date();
		String strdataGenericoUtentiFree1 = "11-04-2014 10:00:44";
		dataGenericoUtentiFree1 = dateformat.parse(strdataGenericoUtentiFree1);
		messGenericoUtentiFree1.setData(dataGenericoUtentiFree1);
		messGenericoUtentiFree1.setOggetto(genericoUtentiFreeThread1.getOggettoThread());
		messGenericoUtentiFree1.setMittente(test3);
		messGenericoUtentiFree1.setDestinatario(test3);
		
		Post genericoUtentiFreePost2 = new Post();
		generaleUtentiFreeSP.getPosts().add(genericoUtentiFreePost2);
		genericoUtentiFreePost2.setSuperpost(generaleUtentiFreeSP);
		genericoUtentiFreePost2.setId(7);
		genericoUtentiFreePost2.setProponente(test2);
		test2.getPostsPartecipati().add(genericoUtentiFreePost2);
		genericoUtentiFreePost2.getPartecipanti().add(test2);
		Thread genericoUtentiFreeThread2 = new Thread();
		genericoUtentiFreePost2.getRisposte().add(genericoUtentiFreeThread2);
		genericoUtentiFreeThread2.setOggettoThread("Post generico fb2");
		genericoUtentiFreeThread2.setMittentePrimo(test2);
		genericoUtentiFreeThread2.setDestinatarioPrimo(test2);
		genericoUtentiFreeThread2.setPost(genericoUtentiFreePost2);
		Messaggio messGenericoUtentiFree3 = new Messaggio();
		messGenericoUtentiFree3.getLetto().add(test2);
		genericoUtentiFreeThread2.getMessaggi().add(messGenericoUtentiFree3);
		messGenericoUtentiFree3.setThread(genericoUtentiFreeThread2);
		messGenericoUtentiFree3.setMessaggio("Salut fb2 post generico!!!");
		Date dataGenericoUtentiFree3 = new Date();
		String strdataGenericoUtentiFree3 = "11-04-2015 10:00:44";
		dataGenericoUtentiFree3 = dateformat.parse(strdataGenericoUtentiFree3);
		messGenericoUtentiFree3.setData(dataGenericoUtentiFree3);
		messGenericoUtentiFree3.setOggetto(genericoUtentiFreeThread2.getOggettoThread());
		messGenericoUtentiFree3.setMittente(test2);
		messGenericoUtentiFree3.setDestinatario(test2);	
//		Messaggio messGenericoUtentiFree4 = new Messaggio();
//		messGenericoUtentiFree4.getLetto().add(test4);
//		genericoUtentiFreeThread2.getMessaggi().add(messGenericoUtentiFree4);
//		messGenericoUtentiFree4.setThread(genericoUtentiFreeThread2);
//		messGenericoUtentiFree4.setMessaggio("Salut post generico(new) - by fb4!!!");
//		Date dataGenericoUtentiFree4 = new Date();
//		String strdataGenericoUtentiFree4 = "11-04-2016 10:00:44";
//		dataGenericoUtentiFree4 = dateformat.parse(strdataGenericoUtentiFree4);
//		messGenericoUtentiFree4.setData(dataGenericoUtentiFree4);
//		messGenericoUtentiFree4.setOggetto("Re: "+genericoUtentiFreeThread2.getOggettoThread());
//		messGenericoUtentiFree4.setMittente(test4);
//		messGenericoUtentiFree4.setDestinatario(test2);
		
		Post genericoUtentiFreePost3 = new Post();
		generaleUtentiFreeSP.getPosts().add(genericoUtentiFreePost3);
		genericoUtentiFreePost3.setSuperpost(generaleUtentiFreeSP);
		genericoUtentiFreePost3.setId(8);
		genericoUtentiFreePost3.setProponente(test4);
		test4.getPostsPartecipati().add(genericoUtentiFreePost3);
		genericoUtentiFreePost3.getPartecipanti().add(test4);
		Thread genericoUtentiFreeThread3 = new Thread();
		genericoUtentiFreePost3.getRisposte().add(genericoUtentiFreeThread3);
		genericoUtentiFreeThread3.setOggettoThread("Hey....... post generico di fb4");
		genericoUtentiFreeThread3.setMittentePrimo(test4);
		genericoUtentiFreeThread3.setDestinatarioPrimo(test4);
		genericoUtentiFreeThread3.setPost(genericoUtentiFreePost3);
		Messaggio messGenericoUtentiFree2 = new Messaggio();
		messGenericoUtentiFree2.getLetto().add(test4);
		genericoUtentiFreeThread3.getMessaggi().add(messGenericoUtentiFree2);
		messGenericoUtentiFree2.setThread(genericoUtentiFreeThread3);
		messGenericoUtentiFree2.setMessaggio("buongiorno sono fb4!!!");
		Date dataGenericoUtentiFree2 = new Date();
		String strdataGenericoUtentiFree2 = "14-08-2015 22:30:21";
		dataGenericoUtentiFree2 = dateformat.parse(strdataGenericoUtentiFree2);
		messGenericoUtentiFree2.setData(dataGenericoUtentiFree2);
		messGenericoUtentiFree2.setOggetto(genericoUtentiFreeThread3.getOggettoThread());
		messGenericoUtentiFree2.setMittente(test4);
		messGenericoUtentiFree2.setDestinatario(test4);
	
		
		SuperPost presentazioneUtentiPlusSP = new SuperPost();
		presentazioneUtentiPlusSP.setNomeSuperPost("PRESENTAZIONe - UTENTi PLUs");
		presentazioneUtentiPlusSP.setId(41);
		
		Post presentazioneUtentiPlusPost0 = new Post();
		presentazioneUtentiPlusPost0.setSuperpost(presentazioneUtentiPlusSP);
		presentazioneUtentiPlusSP.getPosts().add(presentazioneUtentiPlusPost0);
		presentazioneUtentiPlusPost0.setId(9);
		presentazioneUtentiPlusPost0.setProponente(admin);
		admin.getPostsPartecipati().add(presentazioneUtentiPlusPost0);
		presentazioneUtentiPlusPost0.getPartecipanti().add(admin);
		presentazioneUtentiPlusPost0.setPostUtentiPlus(true);
		Thread presentatiPlusThread0 = new Thread();
//		presentatiPlusThread0.setNuovoMessaggio(true);
		presentazioneUtentiPlusPost0.getRisposte().add(presentatiPlusThread0);
		presentatiPlusThread0.setOggettoThread("PRESENTAZIONe - UTENTi PLUs");
		presentatiPlusThread0.setMittentePrimo(admin);
		presentatiPlusThread0.setDestinatarioPrimo(admin);
		presentatiPlusThread0.setPost(presentazioneUtentiPlusPost0);
		Messaggio messPresentatiPlus0 = new Messaggio();
		messPresentatiPlus0.getLetto().add(admin);
		presentatiThread0.getMessaggi().add(messPresentatiPlus0);
		messPresentatiPlus0.setThread(presentatiPlusThread0);
		messPresentatiPlus0.setMessaggio("Sezione dedicata alla presentazione degli utenti plus.");
		Date dataPresentatiPlus0 = new Date();
		String strdataPresentatiPlus0 = "01-01-1999 00:00:00";
		dataPresentatiPlus0 = dateformat.parse(strdataPresentatiPlus0);
		messPresentatiPlus0.setData(dataPresentatiPlus0);
		messPresentatiPlus0.setOggetto(presentatiPlusThread0.getOggettoThread());
		messPresentatiPlus0.setMittente(admin);
		messPresentatiPlus0.setDestinatario(admin);
		
		SuperPost generaleUtentiPlusSP = new SuperPost();
		generaleUtentiPlusSP.setNomeSuperPost("GENERALe - UTENTi PLUs");
		generaleUtentiPlusSP.setId(61);
		
		Post genericoUtentiPlusPost0 = new Post();
		genericoUtentiPlusPost0.setSuperpost(generaleUtentiPlusSP);
		generaleUtentiPlusSP.getPosts().add(genericoUtentiPlusPost0);
		genericoUtentiPlusPost0.setId(10);
		genericoUtentiPlusPost0.setProponente(admin);
		admin.getPostsPartecipati().add(genericoUtentiPlusPost0);
		genericoUtentiPlusPost0.getPartecipanti().add(admin);
		genericoUtentiPlusPost0.setPostUtentiPlus(true);
		Thread genericoUtentiPlusThread0 = new Thread();
		genericoUtentiPlusPost0.getRisposte().add(genericoUtentiPlusThread0);
		genericoUtentiPlusThread0.setOggettoThread("GENERALe - UTENTi PLUs");
		genericoUtentiPlusThread0.setMittentePrimo(admin);
		genericoUtentiPlusThread0.setDestinatarioPrimo(admin);
		genericoUtentiPlusThread0.setPost(genericoUtentiPlusPost0);
		Messaggio messGenericoUtentiPlus0 = new Messaggio();
		messGenericoUtentiPlus0.getLetto().add(admin);
		genericoUtentiPlusThread0.getMessaggi().add(messGenericoUtentiPlus0);
		messGenericoUtentiPlus0.setThread(genericoUtentiPlusThread0);
		messGenericoUtentiPlus0.setMessaggio("Sezione generica per gli utenti plus.");
		Date dataGenericoUtentiPlus0 = new Date();
		String strdataGenericoUtentiPlus0 = "01-01-1999 00:00:00";
		dataGenericoUtentiFree0 = dateformat.parse(strdataGenericoUtentiPlus0);
		messGenericoUtentiPlus0.setData(dataGenericoUtentiPlus0);
		messGenericoUtentiPlus0.setOggetto(genericoUtentiPlusThread0.getOggettoThread());
		messGenericoUtentiPlus0.setMittente(admin);
		messGenericoUtentiPlus0.setDestinatario(admin);

		
		SuperPost collaborazioniFreeSP = new SuperPost();
		collaborazioniFreeSP.setNomeSuperPost("COLLABORAZIONi UTENTi FREe");
		collaborazioniFreeSP.setId(121);
		SuperPost segnalazioniFreeSP = new SuperPost();
		segnalazioniFreeSP.setNomeSuperPost("SEGNALAZIONi UTENTi FREe");
		segnalazioniFreeSP.setId(141);
		
		SuperPost collaborazioniPlusSP = new SuperPost();
		collaborazioniPlusSP.setNomeSuperPost("COLLABORAZIONi UTENTi PLUs");
		collaborazioniPlusSP.setId(161);
		SuperPost segnalazioniPlusSP = new SuperPost();
		segnalazioniPlusSP.setNomeSuperPost("SEGNALAZIONi UTENTi PLUs");
		segnalazioniPlusSP.setId(181);
		
		SuperPost richiestaOffertaSP = new SuperPost();
		richiestaOffertaSP.setNomeSuperPost("RICHIESTa & OFFERTa");
		richiestaOffertaSP.setId(201);
		SuperPost eventiConcorsiSP = new SuperPost();
		eventiConcorsiSP.setNomeSuperPost("EVENTi & CONCORSi");
		eventiConcorsiSP.setId(221);		

		SuperPost liberatorieSP = new SuperPost();
		liberatorieSP.setNomeSuperPost("LIBERATORIe");
		liberatorieSP.setId(241);
		SuperPost strumentazioneTecnicheSP = new SuperPost();
		strumentazioneTecnicheSP.setNomeSuperPost("STRUMENTAZIONe & TECNICHe");
		strumentazioneTecnicheSP.setId(242);
		SuperPost postProduzioneStampaSP = new SuperPost();
		postProduzioneStampaSP.setNomeSuperPost("POSt-PRODUZIONe & STAMPa");
		postProduzioneStampaSP.setId(261);
	
		SuperPost stilistiSfilateSP = new SuperPost();
		stilistiSfilateSP.setNomeSuperPost("RICHIESTa & OFFERTa");
		stilistiSfilateSP.setId(281);
		SuperPost trendsSP = new SuperPost();
		trendsSP.setNomeSuperPost("TRENDs");
		trendsSP.setId(301);
		
		
//TOPICS FORUM **********************************************************		
		TopicForum topicRegolamento = new TopicForum();
		topicRegolamento.setNomeTopicForum("REGOLAMENTi - FORUm & SITo");
		topicRegolamento.setId(1);
		regolamentoForumSP.setTopicforum(topicRegolamento);
		topicRegolamento.getSuperPosts().add(regolamentoForumSP);
		regolamentoSitoSP.setTopicforum(topicRegolamento);
		topicRegolamento.getSuperPosts().add(regolamentoSitoSP);

//		TopicForum topicRegolamentoSito = new TopicForum();
//		topicRegolamentoSito.setNomeTopicForum("REGOLAMENTo SITo");
//		topicRegolamentoSito.setId(2);
		
		TopicForum topicUtentiFree= new TopicForum();
		topicUtentiFree.setNomeTopicForum("UTENTi FREe");
		topicUtentiFree.setId(2);
		presentazioneUtentiFreeSP.setTopicforum(topicUtentiFree);
		topicUtentiFree.getSuperPosts().add(presentazioneUtentiFreeSP);
		generaleUtentiFreeSP.setTopicforum(topicUtentiFree);
		topicUtentiFree.getSuperPosts().add(generaleUtentiFreeSP);		
		
		TopicForum topicUtentiPlus = new TopicForum();
		topicUtentiPlus.setNomeTopicForum("UTENTi PLUs");
		topicUtentiPlus.setId(3);
		generaleUtentiPlusSP.setTopicforum(topicUtentiPlus);
		topicUtentiPlus.getSuperPosts().add(generaleUtentiPlusSP);
		presentazioneUtentiPlusSP.setTopicforum(topicUtentiPlus);
		topicUtentiPlus.getSuperPosts().add(presentazioneUtentiPlusSP);
		
		TopicForum topicFeedbackFree = new TopicForum();
		topicFeedbackFree.setNomeTopicForum("FEEDBACk UTENTi FREe");
		topicFeedbackFree.setId(4);
		collaborazioniFreeSP.setTopicforum(topicFeedbackFree);
		topicFeedbackFree.getSuperPosts().add(collaborazioniFreeSP);
		segnalazioniFreeSP.setTopicforum(topicFeedbackFree);
		topicFeedbackFree.getSuperPosts().add(segnalazioniFreeSP);
		
		TopicForum topicFeedbackPlus = new TopicForum();
		topicFeedbackPlus.setNomeTopicForum("FEEDBACk UTENTi PLUs");
		topicFeedbackPlus.setId(5);
		collaborazioniPlusSP.setTopicforum(topicFeedbackPlus);
		topicFeedbackPlus.getSuperPosts().add(collaborazioniPlusSP);
		segnalazioniPlusSP.setTopicforum(topicFeedbackPlus);
		topicFeedbackPlus.getSuperPosts().add(segnalazioniPlusSP);
		
		TopicForum topicLavoro = new TopicForum();
		topicLavoro.setNomeTopicForum("ANNUNCi LAVORo");
		topicLavoro.setId(6);
		richiestaOffertaSP.setTopicforum(topicLavoro);
		topicLavoro.getSuperPosts().add(richiestaOffertaSP);
		eventiConcorsiSP.setTopicforum(topicLavoro);
		topicLavoro.getSuperPosts().add(eventiConcorsiSP);
		
		TopicForum topicFotografia = new TopicForum();
		topicFotografia.setNomeTopicForum("FOTOGRAFIa");
		topicFotografia.setId(7);
		liberatorieSP.setTopicforum(topicFotografia);
		topicFotografia.getSuperPosts().add(liberatorieSP);
		strumentazioneTecnicheSP.setTopicforum(topicFotografia);
		topicFotografia.getSuperPosts().add(strumentazioneTecnicheSP);
		postProduzioneStampaSP.setTopicforum(topicFotografia);
		topicFotografia.getSuperPosts().add(postProduzioneStampaSP);
		
		TopicForum topicModa = new TopicForum();
		topicModa.setNomeTopicForum("MODa");
		topicModa.setId(8);
		stilistiSfilateSP.setTopicforum(topicModa);
		topicModa.getSuperPosts().add(stilistiSfilateSP);
		trendsSP.setTopicforum(topicModa);
		topicModa.getSuperPosts().add(trendsSP);
		
		
	//////////////////////////////////////////////////////////////
		
		serv.deleteAll(Album.class, em);
		serv.deleteAll(Foto.class, em);
		serv.deleteAll(Utente.class, em);
		serv.deleteAll(TipoLavoro.class, em);
		serv.deleteAll(TipoUtente.class, em);		
		serv.deleteAll(Membership.class, em);
		serv.deleteAll(TipoMembership.class, em);
		serv.deleteAll(Post.class, em);
		serv.deleteAll(Messaggio.class, em);
		serv.deleteAll(Thread.class, em);
		serv.deleteAll(SuperPost.class, em);
		serv.deleteAll(Annuncio.class, em);
		serv.deleteAll(TopicForum.class, em);
		
		serv.persist(admin, em);
		serv.persist(test, em);	
		serv.persist(test2, em);
		serv.persist(test3, em);
		serv.persist(test4, em);
//		serv.persist(regolamentoForumPost, em);
//		serv.persist(regolamentoSitoPost, em);
		serv.persist(regolamentoForumSP, em);
		serv.persist(regolamentoSitoSP, em);
		serv.persist(presentazioneUtentiFreeSP, em);
		serv.persist(generaleUtentiFreeSP, em);
		serv.persist(generaleUtentiPlusSP, em);
		serv.persist(presentazioneUtentiPlusSP, em);
		serv.persist(topicRegolamento, em);
		serv.persist(topicUtentiFree, em);
		serv.persist(topicUtentiPlus, em);
		serv.persist(topicFeedbackFree, em);
		serv.persist(topicFeedbackPlus, em);
		serv.persist(topicLavoro, em);
		serv.persist(topicFotografia, em);
		serv.persist(topicModa, em);
		
		
		serv.persist(free, em);
		serv.persist(plus, em);
		serv.persist(testMShip, em);
		serv.persist(test2MShip, em);
		serv.persist(test3MShip, em);
		serv.persist(test4MShip, em);
		serv.persist(adminMShip, em);
		
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
