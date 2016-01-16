package com.bolo.photoshooters.temp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
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
import com.bolo.photoshooters.web.ContentBean;
import com.bolo.photoshooters.web.EMF;
import com.bolo.photoshooters.web.LoginBean;
import com.bolo.photoshooters.web.UtenteBean;


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
//					serv.merge(utenteBean.getUtente());
//					serv.refresh(utenteBean.getListaAlbum());
					System.out.println("PERSIST************");
					serv.persist(newAlbum);
//					serv.refresh(utenteBean.getUtente().getPubblicati());
//					serv.refresh(utenteBean.getNuovoAlbum().getId());
//					serv.merge(utenteBean.getNuovoAlbum());
				} catch (Exception e) {
					e.printStackTrace();
					String mm = e.getMessage()+" ERRORe CREAZIONe NUOVo ALBUm!";
					contentBean.setMessaggio(mm);
				}				
				contentBean.setMessaggio("Album aggiunto");
				nuovoAlbum.setTitolo(null);
				nuovoAlbum.setDescrizione(null);
				visualizzaAlbums(utenteBean.getUtente());
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

	
	public void cancellaAlbum (int idAlbum){
		EntityManager em = EMF.createEntityManager();
		String hql = "from Album a where a.id=:n";
		Query q = em.createQuery(hql, Album.class);
		q.setParameter("n", idAlbum);
		System.out.println("ALBUM DA CANCELLARE trovato::"+q.getResultList().get(0).toString());
			
		try {
			//cancella cartella album
			Album daCancellare = (Album) q.getResultList().get(0);
			String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator;
			File userAlbumFolder = new File(userAlbumFolderPath + daCancellare.getTitolo() + File.separator);
			if (userAlbumFolder.exists()) {
				System.out.println("ALBUM DA CANCELLARE CARTELLA=="+userAlbumFolder);
				userAlbumFolder.delete();
			}	
			serv.delete((Album)q.getResultList().get(0));
			serv.refresh(utenteBean.getUtente());
//			serv.persist(getUtente());
//			getUtente().getPubblicati().remove((Album)q.getResultList().get(0));
//			serv.refresh(getUtente().getPubblicati());
//			serv.merge(getRisultatoAlbum());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Album CANCELLATO!");
		visualizzaAlbums(utenteBean.getUtente());
	}	

	
	public void titoloAlbumDaModificareSet (String tit){
	setTitoloAlbumDaModificare(tit);
	System.out.println("titoloooo***************"+tit);
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
//			albumDaModificare = new Album();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Album AGGIORNATOOOOOOOOO");
		visualizzaAlbums(utenteBean.getUtente());
		//FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
	}

	
	public void visualizzaAlbums (Utente utente){
		
		EntityManager em = EMF.createEntityManager();

		String hql = "from Album a where a.pubblicatore=:n";
		Query q = em.createQuery(hql, Album.class);
		q.setParameter("n", utente);
		
		listaAlbum = (List<Album>) q.getResultList();
	}
	
	
	public void visualizzaAlbum(int albumId){
		
		EntityManager em = EMF.createEntityManager();

		String hql = "from Album a where a.id=:n";
		Query q = em.createQuery(hql, Album.class);
		q.setParameter("n", albumId);
		
		albumVisualizzato = (Album) q.getResultList().get(0);
//		contentBean.setContent("albums3.xhtml");
	}
	
	
	public List<Album> albumsUtenteTrovato (int idUtente){
		
		EntityManager em = EMF.createEntityManager();

		String hql = "from Album a where a.pubblicatore.id=:n";
		Query q = em.createQuery(hql, Album.class);
		q.setParameter("n", idUtente);
		
		return (List<Album>) q.getResultList();
	}
	
	
	public void uploadFoto() {
		System.out.println("uploadFoto=======================================================================================");
		if (part==null){
			System.out.println("PART==NULL");
			return;
		}				
		//non fa upload di foto con stesso titolo
		for (Foto iesima : albumVisualizzato.getFotos()) {
			System.out.println("FOTO IESIM album.getfotod=="+iesima.getTitolo());
			if (iesima.getTitolo().equals(getNuovaFoto().getTitolo())) {
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
//					System.out.println("DIMENSIONI"+sii.getHeight()+sii.getWidth());
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
//				System.out.println("CoLLabORATORI SIZE== "+nuovaFoto.getCollaboratori().size());
				if(nuovaFoto.getCollaboratori()!=null)
				{
					for (Utente ut :nuovaFoto.getCollaboratori()) {
						System.out.println("COLLABORATORI"+ut.getUsername());
						newFoto.getCollaboratori().add(ut);
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
		
			System.out.println("Fine creazione (merge) nuovaFOTo");
		
				try {
					serv.merge(albumVisualizzato);
//					serv.merge(utenteBean.getUtente());
//					serv.refresh(utenteBean.getUtente());
				} catch (Exception e) {
					e.printStackTrace();
					String mm = e.getMessage()+" ERRORe UPLOAd FOTo nel merge!";
					contentBean.setMessaggio(mm);
				}
				visualizzaFotos(getIdAlbumVisualizzato());

				System.out.println("FINE INPUT FOTO////titolo="+nuovaFoto.getTitolo());
				nuovaFoto.setTitolo(null);
				nuovaFoto.setDescrizione(null);
				nuovaFoto.setSoggetto(null);
				nuovaFoto.setFotografo(null);
//				nuovaFoto.getCollaboratori().clear();
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
		System.out.println("RISULTATOFOToS SIZE=="+risultatoFotos.size());
		System.out.println("albumVisualizzatoTITOLO=="+albumVisualizzato.getTitolo());
		System.out.println("VisualizzaFotos-ALBUMID=="+albumId);

		visualizzaAlbum(albumId);
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
	
	
	public void fotosUtenteTrovato (int albumId){
		
		System.out.println("fotosUtenteTrovato");
		risultatoFotosUtenteTrovato.clear();
		EntityManager em = EMF.createEntityManager();

		String hql = "from Foto f where f.album.id=:n";
		Query q = em.createQuery(hql, Foto.class);
		q.setParameter("n", albumId);

		risultatoFotosUtenteTrovato = (List<Foto>) q.getResultList();

		visualizzaAlbum(albumId);
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
			setIdAlbumVisualizzato(albumId);
	}

	
	FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();

     //Obtener parametros del request
//    Map<String, String> parameterMap = (Map<String, String>) externalContext.getRequestParameterMap();
//    long iduser = Long.valueOf(parameterMap.get("id_usuario"));
	
//	Map<String, String> parameterMap = (Map<String, String>) externalContext.getRequestParameterMap();
//	String param = parameterMap.get("paramName");

//	private String titoloFotoModifica = parameterMap.get("fotosform:my-gallery:1:modificafotoform:tit");
//	private String descrizioneFotoModifica = parameterMap.get("fotosform:my-gallery:1:modificafotoform:des");
//	private String dataFotoModifica = parameterMap.get("dat");
//	private String luogoFotoModifica = parameterMap.get("luo");
//	private String soggettoFotoModifica = parameterMap.get("sog");
//	private String fotografoFotoModifica = parameterMap.get("fot");
//	private String collaboratoriFotoModifica = parameterMap.get("col");
//	private String vietataMinoriFotoModifica = parameterMap.get("vie");
//	private String visibileSoloMembriFotoModifica = parameterMap.get("vis");
//	private String copertinaFotoModifica = parameterMap.get("cop");
	
//    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//    private String titoloFotoModifica = ec.getRequestParameterMap().get("fotosform:my-gallery:1:modificafotoform:tit");
//    private String descrizioneFotoModifica = ec.getRequestParameterMap().get("fotosform:my-gallery:1:modificafotoform:des");
	
	
	public void aggiornaFoto (){
		
		System.out.println("AggiornaFoto1111-start----IDFOTODAMODIFICARE==="+fotoDaModificare.getDescrizione());
//		EntityManager em = EMF.createEntityManager();
//		String hql = "from Foto f where f.id=:n";
//		Query q = em.createQuery(hql, Foto.class);
//		q.setParameter("n", idFoto);

		try {
//			Foto f = (Foto) q.getResultList().get(0);
//			f.setTitolo(fotoDaModificare.getTitolo());
//			f.setDescrizione(fotoDaModificare.getDescrizione());
//			f.setDataFoto(fotoDaModificare.getDataFoto());
//			f.setLuogoScatto(fotoDaModificare.getLuogoScatto());
//			f.setFotografo(fotoDaModificare.getFotografo());
//			f.setSoggetto(fotoDaModificare.getSoggetto());
//			
//			for (Utente coll : fotoDaModificare.getCollaboratori()) {
//				f.getCollaboratori().add(coll);
//			}
//			f.setVietataMinori(fotoDaModificare.isVietataMinori());
//			f.setVisibileSoloMembri(fotoDaModificare.isVisibileSoloMembri());
//			System.out.println("FOTO DA AGGIORNARE trovata::"+f.getTitolo()+"--IDFoto="+f.getId());
			
			serv.merge(fotoDaModificare);
			fotoDaModificare = new Foto();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Foto AGGIORNATA111!");
		visualizzaFotos(albumVisualizzato.getId());
		//FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
	}
	
	
    public void modificaFoto (Foto foto){
		
		System.out.println("MOFIFICAFOTO-----------------start");	
		setFotoDaModificare(foto);
		setIdFotoDaModificare(foto.getId());

		contentBean.setContent("modificaFoto.xhtml");
		//FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("content");
		
	}
	
	
	
	public void cancellaFoto (int idFoto){
		EntityManager em = EMF.createEntityManager();
		String hql = "from Foto f where f.id=:n";
		Query q = em.createQuery(hql, Foto.class);
		q.setParameter("n", idFoto);
		System.out.println("FOTO DA CANCELLARE trovata::"+q.getResultList().get(0).toString());
		try {
			// cancella file foto da server
			String albumName = albumVisualizzato.getTitolo();
			String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator + albumName + File.separator;
			
			if (q.getResultList().size()>0) {
			Foto f = (Foto)q.getResultList().get(0);
			String fileName = f.getNomeFileFoto();
			File outputFilePath = new File(userAlbumFolderPath + fileName);	
			outputFilePath.delete();
			System.out.println("FILE FOTO CANCELLATO!");
			}
			serv.delete((Foto)q.getResultList().get(0));
			serv.refresh(utenteBean.getUtente());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Foto CANCELLATA!");
		visualizzaFotos(albumVisualizzato.getId());
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