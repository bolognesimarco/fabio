package com.bolo.photoshooters.temp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

import com.bolo.photo.web.entity.Album;
import com.bolo.photo.web.entity.Foto;
import com.bolo.photo.web.entity.RegioneItaliana;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.web.ContentBean;
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
	
	public void nuovoAlbum () {
		
		String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator;
		File userAlbumFolder = new File(userAlbumFolderPath + utenteBean.getNewAlbumName() + File.separator);
		if (!userAlbumFolder.exists()) {
			if (userAlbumFolder.mkdirs()) {
				System.out.println("Cartella nuovo album creata!");
				Album nuovoAlbum = new Album();
				nuovoAlbum.setTitolo(utenteBean.getNewAlbumName());
				nuovoAlbum.setPubblicatore(utenteBean.getUtente());
				utenteBean.getUtente().getPubblicati().add(nuovoAlbum);

				try {
					serv.merge(utenteBean.getUtente());
				} catch (Exception e) {
					e.printStackTrace();
					String mm = e.getMessage()+" ERRORe CREAZIONe NUOVo ALBUm!";
					contentBean.setMessaggio(mm);
				}
				
				contentBean.setMessaggio("Album aggiunto");
				utenteBean.setNewAlbumName(null);
			} else {
				System.out.println("Errore nella creazione della cartella dell'album!");
				contentBean.setMessaggio("errore nell'aggiunta album");
			}
		} else {
			contentBean.setMessaggio("Album già esistente!2");
//			statusMessage = "Album già esistente!";
			System.out.println("Album già esistente!");
		}
	}
	
	
	public void uploadFoto() {
		
		// Extract file name from content-disposition header of file part
		String fileName = getFileName(part);
		String albumName = utenteBean.getRisultatoAlbum().getTitolo();
		String radomFotoName = UUID.randomUUID().toString();

		String userFotoFileName = radomFotoName + "." + getFileExtension(fileName);
		String userAlbumFolderPath = "C:" + File.separator + "temp" + File.separator + utenteBean.getUtente().getUsername() + File.separator + albumName + File.separator;
		
//		File userAlbumFolder = new File(userAlbumFolderPath + albumName + File.separator);
		statusMessage="";
		if (fileName!="") {

				statusMessage="";
				Foto newFoto = new Foto();
				newFoto.setAlbum(utenteBean.getRisultatoAlbum());
				newFoto.setFotografo(utenteBean.getUtente());
				newFoto.setTitolo(utenteBean.getNewFotoName());
				newFoto.setPubblicatore(utenteBean.getUtente());
//				utenteBean.getRisultatoAlbum().getFotos().add(newFoto);
				utenteBean.getUtente().getFotografoDi().add(newFoto);

				try {
					serv.merge(utenteBean.getUtente());
				} catch (Exception e) {
					e.printStackTrace();
					String mm = e.getMessage()+" ERRORe UPLOAd FOTo!";
					contentBean.setMessaggio(mm);
				}
				
				utenteBean.setNewFotoName(null);
				utenteBean.visualizzaFotos(utenteBean.getAlbumId());
			} else {
				System.out.println("Errore nell'upload della foto!");
			}

		
		File outputFilePath = new File(userAlbumFolderPath + userFotoFileName);
		
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
//		for (Foto tit : utenteBean.getRisultatoAlbum().getFotos()) {
//			System.out.println("FOTOOOOOOOOOOO---"+tit.getTitolo());
//		}
	}


	public void uploadAvatar() {
		
		// Extract file name from content-disposition header of file part
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
	
}