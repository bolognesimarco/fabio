package com.bolo.photoshooters.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.Part;

import org.eclipse.persistence.internal.sessions.remote.SequencingFunctionCall.GetNextValue;

import com.bolo.photo.web.entity.Album;
import com.bolo.photo.web.entity.Foto;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photo.web.entity.Voto;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.service.SimpleImageInfo;
import com.bolo.photoshooters.web.EMF;


@ManagedBean(name = "inputBean")
@SessionScoped
public class InputBean {
 
	private Part part;
	private String statusMessage;
	private ServiziComuni serv = new ServiziComuniImpl();
	@ManagedProperty(value="#{contentBean}")
	private ContentBean contentBean;
	
	@ManagedProperty(value="#{utenteBean}")
	private UtenteBean utenteBean;

	private Foto nuovaFoto = new Foto();
	List<Foto> risultatoFotos = new ArrayList<Foto>();
	private boolean FotoCopertinaAlbum = false;
	private boolean FotoCopertinaAlbumDaModificare = false;
	List<Foto> risultatoFotosUtenteTrovato = new ArrayList<Foto>();
	private Foto fotoDaModificare = new Foto();
	private int idFotoDaModificare;
	private Foto fotoDaVotare = new Foto();
	private Voto votoFoto = new Voto();
	List<Album> listaAlbum = new ArrayList<Album>();
	Album albumVisualizzato = new Album();
	private Album nuovoAlbum = new Album(); 
	private int idAlbumVisualizzato = 0;
	private Album albumDaModificare = new Album();
	private String titoloAlbumDaModificare = ""; 
	
	
	public void nuovoAlbum () {
		System.out.println("funzione nuovoAlbum() start!!!");
		String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator;
		File userAlbumFolder = new File(userAlbumFolderPath + nuovoAlbum.getTitolo() + File.separator);
		if (!userAlbumFolder.exists()) {
			if (userAlbumFolder.mkdirs()) {
				System.out.println("Cartella nuovo album creata!");
				Album newAlbum = new Album();
				newAlbum.setTitolo(nuovoAlbum.getTitolo());
				newAlbum.setDescrizione(nuovoAlbum.getDescrizione());
				newAlbum.setPubblicatore(utenteBean.getUtente());
				utenteBean.getUtente().getPubblicati().add(newAlbum);
				listaAlbum.add(newAlbum);
				System.out.println("nuovoAlbum ID==:"+newAlbum.getId());
				try {
					System.out.println("PERSIST************");
					serv.persist(newAlbum);
					for (Utente ut : utenteBean.cercaFollowersUtente(utenteBean.getUtente())) {
						if (ut.isMailNuovoAlbumDiUtenteSeguito()) {
							MailSender.sendNuovoAlbumUtenteSeguitoMail(ut.getEmail(), utenteBean.getUtente().getUsername());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					String mm = e.getMessage()+" ERRORe CREAZIONe NUOVo ALBUm!";
					contentBean.setMessaggio(mm);
				}				
				contentBean.setMessaggio("Album aggiunto");
				nuovoAlbum.setTitolo(null);
				nuovoAlbum.setDescrizione(null);
//				visualizzaAlbums(utenteBean.getUtente());
			} else {
				System.out.println("Errore nella creazione della cartella dell'album!");
				contentBean.setMessaggio("errore nell'aggiunta album");
			}
		} else {
			System.out.println("GETTITOLO:"+getNuovoAlbum().getTitolo());
			System.out.println("GEDESCRIZIONE:"+getNuovoAlbum().getDescrizione());
			contentBean.setMessaggio("Album già esistente!2");
//			statusMessage = "Album già esistente!";
			System.out.println("Album già esistente!");
		}
	}
    
	
	public void cancellaAlbum (Album daCancellare){
		System.out.println("funzione cancellaAlbum start ////albumid=="+daCancellare.getId());
//		EntityManager em = EMF.createEntityManager();
//		String hql = "from Album a where a.id=:n";
//		Query q = em.createQuery(hql, Album.class);
//		q.setParameter("n", album.getId());
//		System.out.println("ALBUM DA CANCELLARE trovato::"+q.getResultList().get(0).toString());		
		try {
			//cancella cartella album
//			Album daCancellare = (Album) q.getResultList().get(0);
			String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator;
			File userAlbumFolder = new File(userAlbumFolderPath + daCancellare.getTitolo() + File.separator);
			if (userAlbumFolder.exists()) {
				System.out.println("ALBUM DA CANCELLARE CARTELLA=="+userAlbumFolder);
				for(File file: userAlbumFolder.listFiles()) {
					file.delete();
				}
				userAlbumFolder.delete();
			}	
			serv.delete(daCancellare);
			Iterator<Album> it = utenteBean.getUtente().getPubblicati().iterator();
			while(it.hasNext()) {
				Album al = it.next();
				if(al.getId()==daCancellare.getId()) {
					it.remove();
				}
			}
//			serv.refresh(utenteBean.getUtente());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Album CANCELLATO!");
		contentBean.setMessaggio("Album CANCELLATO!");
		contentBean.setContent("albums3.xhtml");
//		visualizzaAlbums(utenteBean.getUtente());
	}	

//	mi serve il vecchio titolo per rinominare la cartella con il nuovo titolo
	public void titoloAlbumDaModificareSet (String tit){
	setTitoloAlbumDaModificare(tit);
	}
	
	
	public void aggiornaAlbum (Album a){	
		System.out.println("AggiornaAlbum-start----==="+titoloAlbumDaModificare);
		try {
			//rinomino cartella se cambio titolo album
			String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator;
			File userAlbumFolder = new File(userAlbumFolderPath + titoloAlbumDaModificare + File.separator);
			System.out.println("ALBUM DA AGGIORNARE CARTELLA=="+userAlbumFolder);
			if (userAlbumFolder.exists()) {
				String newUserAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator;
				File newUserAlbumFolder = new File(newUserAlbumFolderPath + a.getTitolo() + File.separator);
				System.out.println("ALBUM DA AGGIORNARE NUOVA CARTELLA=="+newUserAlbumFolder);
				userAlbumFolder.renameTo(newUserAlbumFolder);
			}				
			serv.merge(a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Album AGGIORNATOOOOOOOOO");
		contentBean.setMessaggio("Album AGGIORNATo!");
//		visualizzaAlbums(utenteBean.getUtente());
	}

	
//	public void visualizzaAlbums (Utente utente){	
//		EntityManager em = EMF.createEntityManager();
//		String hql = "from Album a where a.pubblicatore.id=:n";
//		Query q = em.createQuery(hql, Album.class);
//		q.setParameter("n", utente.getId());		
//		listaAlbum = (List<Album>) q.getResultList();
////		listaAlbum = utente.getPubblicati();
//	}
	
	
//	public void visualizzaAlbum(int albumId){		
//		EntityManager em = EMF.createEntityManager();
//		String hql = "from Album a where a.id=:n";
//		Query q = em.createQuery(hql, Album.class);
//		q.setParameter("n", albumId);		
//		albumVisualizzato = (Album) q.getResultList().get(0);
////		contentBean.setContent("albums3.xhtml");
//	}
	
	
//	public List<Album> albumsUtenteTrovato (int idUtente){		
//		EntityManager em = EMF.createEntityManager();
//		String hql = "from Album a where a.pubblicatore.id=:n";
//		Query q = em.createQuery(hql, Album.class);
//		q.setParameter("n", idUtente);	
//		return (List<Album>) q.getResultList();
//	}
	
	
	public void uploadFoto() {
		System.out.println("uploadFoto==========================================");
		if (part==null){
			System.out.println("PART==NULL");
			return;
		}				
		//non fa upload di foto con stesso titolo
		for (Foto iesima : albumVisualizzato.getFotos()) {
			System.out.println("FOTO IESIM album.getfotod=="+iesima.getTitolo());
			if (iesima.getTitolo().equals(getNuovaFoto().getTitolo())) {
				contentBean.setMessaggio("Non è possibile caricare foto con lo stesso titolo nello stesso album.");
				return;
			}
		}
		// Extract file name from content-disposition header of file part
		String fileName = getFileName(part);
		String albumName = albumVisualizzato.getTitolo();
		String radomFotoName = UUID.randomUUID().toString();
		String userFotoFileName = radomFotoName + "." + getFileExtension(fileName);
		String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator + albumName + File.separator;
		
		System.out.println("Inizio upload FOTo"+userAlbumFolderPath+userFotoFileName);	
		File outputFilePath = new File(userAlbumFolderPath + userFotoFileName);		
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = part.getInputStream();
			outputStream = new FileOutputStream(outputFilePath);
 
			int read = 0;
			final byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			statusMessage = "Upload foto completato!";		
		} catch (IOException e) {
			e.printStackTrace();
			statusMessage = "Upload foto fallito!";
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if (!fileName.equals("")) {		
			Foto newFoto = new Foto();
			try {
					SimpleImageInfo sii = new SimpleImageInfo(outputFilePath);
					newFoto.setAltezzaFoto(sii.getHeight());
					newFoto.setLarghezzaFoto(sii.getWidth());
					SimpleImageInfo imageInfo = new SimpleImageInfo((outputFilePath));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			}			
			System.out.println("inizio crazione nuovaFOTo");				
				newFoto.setAlbum(albumVisualizzato);
				newFoto.setPubblicatore(utenteBean.getUtente());
				newFoto.setFotografo(nuovaFoto.getFotografo());
				System.out.println("titolo nuovafoto="+nuovaFoto.getTitolo());
				newFoto.setTitolo(nuovaFoto.getTitolo());
				newFoto.setDescrizione(nuovaFoto.getDescrizione());
				newFoto.setNomeFileFoto(userFotoFileName);
				newFoto.setSoggetto(nuovaFoto.getSoggetto());
				newFoto.setLuogoScatto(nuovaFoto.getLuogoScatto());
				newFoto.setVietataMinori(nuovaFoto.isVietataMinori());
				newFoto.setVisibileSoloMembri(nuovaFoto.isVisibileSoloMembri());
				if(nuovaFoto.getCollaboratori()!=null) {
					for (Utente ut :nuovaFoto.getCollaboratori()) {
						newFoto.getCollaboratori().add(ut);
						ut.getCollaboratori().add(utenteBean.getUtente());
						utenteBean.getUtente().getCollaboratori().add(ut);
						try {
							System.out.println("merge - "+ut.getUsername());
							serv.merge(ut);
							serv.merge(utenteBean.getUtente());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}			
				if (nuovaFoto.getDataFoto()==null){
					Date currentDate = new Date();
					newFoto.setDataFoto(currentDate);	
				}
				else{
					newFoto.setDataFoto(nuovaFoto.getDataFoto());
				}			
				if (isFotoCopertinaAlbum()) {
					albumVisualizzato.setCopertinaAlbum(newFoto);
				}
				albumVisualizzato.getFotos().add(newFoto);	
			System.out.println("Fine creazione (merge) nuovaFOTo - getFOTOS.SIZE=="+albumVisualizzato.getFotos().size());
			System.out.println("UploadNewfoto.getId=="+newFoto.getId());
				try {
//					serv.persist(albumVisualizzato);
					serv.persist(newFoto);
				} catch (Exception e) {
					e.printStackTrace();
					String mm = e.getMessage()+" ERRORe UPLOAd FOTo nel merge!";
					contentBean.setMessaggio(mm);
				}
//				visualizzaFotos2(albumVisualizzato);
				System.out.println("DOPO merge - Newfoto.getId=="+newFoto.getId());
				System.out.println("FINE INPUT FOTO////titolo="+nuovaFoto.getTitolo());
				nuovaFoto.setTitolo(null);
				nuovaFoto.setDescrizione(null);
				nuovaFoto.setSoggetto(null);
				nuovaFoto.setFotografo(null);
				nuovaFoto.setCollaboratori(null);
				nuovaFoto.setDataFoto(null);
				nuovaFoto.setLuogoScatto(null);
				nuovaFoto.setVietataMinori(false);
				nuovaFoto.setVisibileSoloMembri(false);
				setFotoCopertinaAlbum(false);
				part=null;			
			} else {
				System.out.println("Errore nell'upload della foto!");
			} 

	}


	public void visualizzaFotos(int albumId){	
		risultatoFotos.clear();
		EntityManager em = EMF.createEntityManager();
		String hql = "from Foto f where f.album.id=:n";
		Query q = em.createQuery(hql, Foto.class);
		q.setParameter("n", albumId);
		risultatoFotos = (List<Foto>) q.getResultList();
//		visualizzaAlbum(albumId);
		pswp.clear();
		for (Foto f : risultatoFotos) {
			PerPhotoswipe pp = new PerPhotoswipe();
			pp.setSrc("/lil?path="+utenteBean.getUtente().getUsername()+"/"+albumVisualizzato.getTitolo()+"/"+f.getNomeFileFoto());
			pp.setH(f.getAltezzaFoto());
			pp.setW(f.getLarghezzaFoto());
			pswp.add(pp);
			System.out.println("PHOTOSWIPE"+pp.getSrc()+pp.getH()+pp.getW()+albumVisualizzato.getTitolo());	
		}
		System.out.println("PSWP=="+pswpS+"FINE");
		contentBean.setContent("fotos.xhtml");
		contentBean.setMessaggio(null);
		setIdAlbumVisualizzato(albumId);
	}


	public void visualizzaFotos2(Album album){	
		System.out.println("visFotos222 - album=="+album.getTitolo());
//		risultatoFotos.clear();
		risultatoFotos = album.getFotos();
		System.out.println("risultatotoFotos.size=="+risultatoFotos.size());
		albumVisualizzato = album; 
		pswp.clear();
		for (Foto f : risultatoFotos) {
			PerPhotoswipe pp = new PerPhotoswipe();
			pp.setSrc("/lil?path="+utenteBean.getUtente().getUsername()+"/"+albumVisualizzato.getTitolo()+"/"+f.getNomeFileFoto());
			pp.setH(f.getAltezzaFoto());
			pp.setW(f.getLarghezzaFoto());
			pswp.add(pp);
			System.out.println("PHOTOSWIPE"+pp.getSrc()+pp.getH()+pp.getW()+albumVisualizzato.getTitolo());	
		}
		System.out.println("PSWP=="+pswpS+"FINE");
		contentBean.setContent("fotos.xhtml");
		contentBean.setMessaggio(null);
	}
	
	
//	public void fotosUtenteTrovato (int albumId){	
//		System.out.println("fotosUtenteTrovato");
//		risultatoFotosUtenteTrovato.clear();
//		EntityManager em = EMF.createEntityManager();
//
//		String hql = "from Foto f where f.album.id=:n";
//		Query q = em.createQuery(hql, Foto.class);
//		q.setParameter("n", albumId);
//
//		risultatoFotosUtenteTrovato = (List<Foto>) q.getResultList();
//		visualizzaAlbum(albumId);
//			pswp.clear();
//			for (Foto f : risultatoFotosUtenteTrovato) {
//				System.out.println("fotosUtenteTrovato - nomeFile=="+f.getNomeFileFoto());
//				PerPhotoswipe pp = new PerPhotoswipe();
//				pp.setSrc("/lil?path="+utenteBean.getCercaUtente().getUtente().getUsername()+"/"+albumVisualizzato.getTitolo()+"/"+f.getNomeFileFoto());
//				pp.setH(f.getAltezzaFoto());
//				pp.setW(f.getLarghezzaFoto());
//				pswp.add(pp);
//			}
//			contentBean.setContent("fotosUtenteTrovato.xhtml");
//			contentBean.setMessaggio(null);
//			setIdAlbumVisualizzato(albumId);
//	}

	
	public void fotosUtenteTrovato2 (Album album) {
		risultatoFotosUtenteTrovato.clear();
		risultatoFotosUtenteTrovato = album.getFotos();
		albumVisualizzato = album; 
			pswp.clear();
			for (Foto f : risultatoFotosUtenteTrovato) {
				System.out.println("fotosUtenteTrovato - nomeFile=="+f.getNomeFileFoto());
				PerPhotoswipe pp = new PerPhotoswipe();
				pp.setSrc("/lil?path="+utenteBean.getCercaUtente().getUtente().getUsername()+"/"+albumVisualizzato.getTitolo()+"/"+f.getNomeFileFoto());
				pp.setH(f.getAltezzaFoto());
				pp.setW(f.getLarghezzaFoto());
				pswp.add(pp);
			}
		contentBean.setContent("fotosUtenteTrovato.xhtml");
		contentBean.setMessaggio(null);
	}
	
	
	FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();


	
	public void aggiornaFoto (){	
		System.out.println("AggiornaFoto1111-start----IDFOTODAMODIFICARE==="+fotoDaModificare.getDescrizione());
		if(FotoCopertinaAlbumDaModificare){
			albumVisualizzato.setCopertinaAlbum(fotoDaModificare);
		} 
		if(fotoDaModificare.getCollaboratori()!=null) {
			for (Utente ut :fotoDaModificare.getCollaboratori()) {
//				newFoto.getCollaboratori().add(ut);
				ut.getCollaboratori().add(utenteBean.getUtente());
				utenteBean.getUtente().getCollaboratori().add(ut);
				try {
					System.out.println("merge - "+ut.getUsername());
					serv.merge(ut);
					serv.merge(utenteBean.getUtente());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			serv.merge(fotoDaModificare);
			serv.merge(albumVisualizzato);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Foto AGGIORNATA111!");
		visualizzaFotos2(albumVisualizzato);
	}
	
	
    public void modificaFoto (Foto foto){	
		System.out.println("MODIFICAFOTO-----------------start");	
		setFotoDaModificare(foto);
		setIdFotoDaModificare(foto.getId());
		setFotoCopertinaAlbumDaModificare(false);
		if(albumVisualizzato.getCopertinaAlbum()!=null){
			if (albumVisualizzato.getCopertinaAlbum().getId()==foto.getId()) {
				setFotoCopertinaAlbumDaModificare(true);
			}			
		}
		contentBean.setContent("modificaFoto.xhtml");
		//FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");	
	}
		
	
	public void cancellaFoto (Foto foto){
		System.out.println("cancellaFoto startttttt fOTO tit=="+foto.getTitolo()+"id="+foto.getId());
//		EntityManager em = EMF.createEntityManager();
//		String hql = "from Foto f where f.id=:n";
//		Query q = em.createQuery(hql, Foto.class);
//		q.setParameter("n", foto.getId());
//		System.out.println("FOTO DA CANCELLARE trovata::"+q.getResultList().get(0).toString());
		try {
			// cancella file foto da server
			String albumName = albumVisualizzato.getTitolo();
			String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator + albumName + File.separator;		
//			if (q.getResultList().size()>0) {
//				Foto f = (Foto)q.getResultList().get(0);
//				String fileName = f.getNomeFileFoto();
			String fileName = foto.getNomeFileFoto();
			File outputFilePath = new File(userAlbumFolderPath + fileName);	
			outputFilePath.delete();
			System.out.println("FILE FOTO CANCELLATO!");
//			}
//			serv.delete((Foto)q.getResultList().get(0));
			serv.delete(foto);
			Iterator<Foto> it = albumVisualizzato.getFotos().iterator();
			while(it.hasNext()) {
				Foto f = it.next();
				if(f.getId()==foto.getId()) {
					it.remove();
				}
			}
			serv.refresh(utenteBean.getUtente());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Foto CANCELLATA!");
//		visualizzaFotos2(albumVisualizzato);
	}
	

	public void uploadAvatar() {	
		// Extract file name from content-disposition header of file part
		if (part==null){
			return;
		}
		String fileName = getFileName(part);
		String userAvatarFileName = "avatar_" + utenteBean.getUtente().getUsername() + "." + getFileExtension(fileName);
		String userFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator;
		File userFolder = new File(userFolderPath);
		
		if (!userFolder.exists()) {
			if (userFolder.mkdirs()) {
				System.out.println("Multiple directories are created!");
			} else {
				System.out.println("Failed to create multiple directories!");
			}
		}
		
		File outputFilePath = new File(userFolderPath + userAvatarFileName);
		utenteBean.getUtente().setAvatar(userAvatarFileName);

		try {
			serv.merge(utenteBean.getUtente());
		} catch (Exception e) {
			e.printStackTrace();
			String mm = e.getMessage()+" ERRORe UPLOAd AVATAr!";
			contentBean.setMessaggio(mm);
		}
		
		// Copy uploaded file to destination path
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = part.getInputStream();
			outputStream = new FileOutputStream(outputFilePath);
 
			int read = 0;
			final byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			statusMessage = "Upload avatar completato! "+utenteBean.getUtente().getAvatar();
		} catch (IOException e) {
			e.printStackTrace();
			statusMessage = "Upload avatar fallito!";
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//return null;    // return to same page
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
	
	private List<PerPhotoswipe> pswp = new ArrayList<InputBean.PerPhotoswipe>();
	private String pswpS = "";
	
	 
		// Extract file name from content-disposition header of file part
		private String getFileName(Part part) {
			final String partHeader = part.getHeader("content-disposition");
			System.out.println("***** partHeader: " + partHeader);
			for (String content : part.getHeader("content-disposition").split(";")) {
				if (content.trim().startsWith("filename")) {
					return content.substring(content.indexOf('=') + 1).trim()
							.replace("\"", "");
				}
			}
			return null;
		}
		
		private static String getFileExtension(String fileName) {
		     //String fileName = file.getName();
		     if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
		     return fileName.substring(fileName.lastIndexOf(".")+1);
		     else return "";
		}
	
	
		public void votaFoto (Foto f) {
			if (votoFoto.getScore()>-1) {
				try {
					votoFoto.setRilasciatoDa(utenteBean.getUtente());
					votoFoto.setFoto(f);
					f.getVoti().add(votoFoto);
					f.setMediaVoti(utenteBean.calcolaMediaVotiFoto(f.getVoti()));
					if(f.getPubblicatore().isMailNuovoVoto()){
						MailSender.sendNuovoVotoMail(f.getPubblicatore().getEmail(), utenteBean.getUtente().getUsername());
					}
					serv.merge(f);
					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("my-gallery");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				votoFoto.setScore(-1);
			}		
		}
		
		
		public boolean fotoGiaVotata (int idFoto, int idUtente) {
			EntityManager em = EMF.createEntityManager();
			String hql = "from Voto v where v.foto.id=:n and v.rilasciatoDa.id=:u";
			Query q = em.createQuery(hql, Voto.class);
			q.setParameter("n", idFoto);
			q.setParameter("u", idUtente);
			if (q.getResultList()!=null && q.getResultList().size()>0) {
				System.out.println("FOTO GIa' VOTATA= TRUE");
				return true;
			}
			System.out.println("FOTO GIa' VOTATA= FALSE");
			return false;
		}
		
		
		public void aggiungiFotoPreferita (Foto foto) {
			System.out.println("aggiungifotoPreferita start");
			foto.getUtentiChePreferisconoFoto().add(utenteBean.getUtente());
				try {
					if(foto.getPubblicatore().isMailNuovaFotoPreferita()){
						MailSender.sendNuovaFotoPreferitaMail(foto.getPubblicatore().getEmail(), utenteBean.getUtente().getUsername());
					}
					serv.merge(foto);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	
		
		public void rimuoviFotoPreferita (Foto foto) {
			System.out.println("rimuovifotoPreferita start");
			Iterator<Utente> it = foto.getUtentiChePreferisconoFoto().iterator();
			while(it.hasNext()) {
				Utente ut = it.next();
				if(ut.getId()==utenteBean.getUtente().getId()) {
					it.remove();
				}
			}
			try {
				serv.merge(foto);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			fotosUtenteTrovato(getIdAlbumVisualizzato());
//			contentBean.setContent("fotosUtenteTrovato.xhtml");
		}
		
		
		public boolean fotoPreferita (Foto foto) {
			for (Utente ut : foto.getUtentiChePreferisconoFoto()) {
				if(utenteBean.getUtente()!=null && ut.getId()==utenteBean.getUtente().getId()) {
					System.out.println("fotoPreferita=TRUE");
					return true;
				}
			}
			System.out.println("fotoPreferita=FALSE");
			return false;
		}	
	
	
		public List<Utente> suggerisciUtenteTranneLoggatoNoDuplica (String username) {			
			System.out.println("suggerisci start");
			
			EntityManager em = EMF.createEntityManager();
			List<Utente> utenti = em
			.createQuery("from Utente u where u.username like :user and u.id<>:idUt")
			.setParameter("user", username+"%")
			.setParameter("idUt", utenteBean.getUtente().getId())
			.getResultList();
			for (Utente utente : utenti) {
				System.out.println("utentiIniziali="+utente.getUsername());
			}
			for (Utente ut : nuovaFoto.getCollaboratori()) {
				System.out.println("collaboratori="+ut.getUsername());
				Iterator<Utente> utIter = utenti.iterator();
				Utente u = null;
				while(utIter.hasNext()){
					u = utIter.next();
					System.out.println("utentiSuggeriti="+u.getUsername());
					if (ut.getId()==u.getId()) {
						System.out.println("utenterimosso="+u.getUsername());
						utIter.remove();
					}
				}
			}
			System.out.println("utentiRimastisiZE="+utenti.size());
			for (Utente utente : utenti) {
				System.out.println("utentiRimasti="+utente.getUsername());
			}
			return utenti;
		}
		
		
		public boolean fotoGiaVisualizzata (Foto f,Utente ut) {
			Iterator<Utente> it = f.getVisualizzatori().iterator();
			while(it.hasNext()) {
				Utente al = it.next();
				if(al.getId()==ut.getId()) {
					return true;
				}
			}
			return false;
		}
		
		
		public void fotoVisualizzata(String nomeFile) {
			System.out.println("visualizzata foto "+nomeFile+" da "+utenteBean.getUtente().getUsername());
			System.out.println("foto visualizzata id="+nomeFile);
			EntityManager em = EMF.createEntityManager();
			List<Foto> f = em
			.createQuery("from Foto f where f.nomeFileFoto =:nf")
			.setParameter("nf", nomeFile)
			.getResultList();
			System.out.println("foto visualizzata id="+nomeFile);
			if(!fotoGiaVisualizzata(f.get(0), utenteBean.getUtente())) {
				f.get(0).getVisualizzatori().add(utenteBean.getUtente());
				Integer visite = f.get(0).getVisite();
				visite++;
				f.get(0).setVisite(visite);
				System.out.println("aggiunto visualizzazione foto id="+f.get(0).getId());
				try {
					serv.merge(f.get(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	//*********GETTERS&SETTERS************
	
		
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
		
	public List<PerPhotoswipe> getPswp() {
		return pswp;
	}

	public void setPswp(List<PerPhotoswipe> pswp) {
		this.pswp = pswp;
	}	
		

	
	
	
	
	public boolean isFotoCopertinaAlbumDaModificare() {
		return FotoCopertinaAlbumDaModificare;
	}

	public void setFotoCopertinaAlbumDaModificare(
			boolean fotoCopertinaAlbumDaModificare) {
		FotoCopertinaAlbumDaModificare = fotoCopertinaAlbumDaModificare;
	}

	public Voto getVotoFoto() {
		return votoFoto;
	}


	public void setVotoFoto(Voto votoFoto) {
		this.votoFoto = votoFoto;
	}


	public Foto getFotoDaVotare() {
		return fotoDaVotare;
	}


	public void setFotoDaVotare(Foto fotoDaVotare) {
		this.fotoDaVotare = fotoDaVotare;
	}


	public String getTitoloAlbumDaModificare() {
		return titoloAlbumDaModificare;
	}


	public void setTitoloAlbumDaModificare(String titoloAlbumDaModificare) {
		this.titoloAlbumDaModificare = titoloAlbumDaModificare;
	}


	public Album getAlbumDaModificare() {
		return albumDaModificare;
	}


	public void setAlbumDaModificare(Album albumDaModificare) {
		this.albumDaModificare = albumDaModificare;
	}


	public int getIdFotoDaModificare() {
		return idFotoDaModificare;
	}


	public void setIdFotoDaModificare(int idFotoDaModificare) {
		this.idFotoDaModificare = idFotoDaModificare;
	}


	public Foto getFotoDaModificare() {
		return fotoDaModificare;
	}


	public void setFotoDaModificare(Foto fotoDaModificare) {
		System.out.println("setFotoDaModificare:"+fotoDaModificare.getDescrizione());
		this.fotoDaModificare = fotoDaModificare;
	}


	public List<Foto> getRisultatoFotosUtenteTrovato() {
		return risultatoFotosUtenteTrovato;
	}


	public void setRisultatoFotosUtenteTrovato(
			List<Foto> risultatoFotosUtenteTrovato) {
		this.risultatoFotosUtenteTrovato = risultatoFotosUtenteTrovato;
	}


	public boolean isFotoCopertinaAlbum() {
		return FotoCopertinaAlbum;
	}


	public void setFotoCopertinaAlbum(boolean fotoCopertinaAlbum) {
		FotoCopertinaAlbum = fotoCopertinaAlbum;
	}


	public Foto getNuovaFoto() {
		return nuovaFoto;
	}


	public int getIdAlbumVisualizzato() {
		return idAlbumVisualizzato;
	}


	public void setIdAlbumVisualizzato(int idAlbumVisualizzato) {
		this.idAlbumVisualizzato = idAlbumVisualizzato;
	}


	public void setNuovaFoto(Foto nuovaFoto) {
		this.nuovaFoto = nuovaFoto;
	}


	public Album getNuovoAlbum() {
		return nuovoAlbum;
	}


	public void setNuovoAlbum(Album nuovoAlbum) {
		this.nuovoAlbum = nuovoAlbum;
	}


	public Album getAlbumVisualizzato() {
		return albumVisualizzato;
	}


	public void setAlbumVisualizzato(Album albumVisualizzato) {
		this.albumVisualizzato = albumVisualizzato;
	}


	public List<Foto> getRisultatoFotos() {
		return risultatoFotos;
	}

	public void setRisultatoFotos(List<Foto> risultatoFotos) {
		this.risultatoFotos = risultatoFotos;
	}

	public List<Album> getListaAlbum() {
		return listaAlbum;
	}

	public void setListaAlbum(List<Album> listaAlbum) {
		this.listaAlbum = listaAlbum;
	}

	public ContentBean getContentBean() {
		return contentBean;
	}

	public void setContentBean(ContentBean contentBean) {
		this.contentBean = contentBean;
	}

	public UtenteBean getUtenteBean() {
		return utenteBean;
	}

	public void setUtenteBean(UtenteBean utenteBean) {
		this.utenteBean = utenteBean;
	}
	public Part getPart() {
		return part;
	}
 
	public void setPart(Part part) {
		this.part = part;
	}
 
	public String getStatusMessage() {
		return statusMessage;
	}
 
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	
}