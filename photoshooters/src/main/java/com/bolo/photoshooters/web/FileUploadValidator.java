package com.bolo.photoshooters.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator("FileUploadValidator")
public class FileUploadValidator implements Validator {
 
	@Override
	public void validate(FacesContext context, UIComponent uiComponent,
			Object value) throws ValidatorException {
 
		Part part = (Part) value;
 
		// 1. validate file name length
		String fileName = getFileName(part);
		System.out.println("----- validator fileName: " + fileName);
		if(fileName.length() == 0 ) {
			FacesMessage message = new FacesMessage("Errore: nome del file non valido!");
			throw new ValidatorException(message);
		} else if (fileName.length() > 50) {
			FacesMessage message = new FacesMessage("Errore: nome del file troppo lungo!");
			throw new ValidatorException(message);
		}
 
		// 2. validate file type (only text files allowed)
		System.out.println(part.getContentType());
		//if (!"image".equals(part.getContentType())) {
		if (!part.getContentType().startsWith("image/")) {
			FacesMessage message = new FacesMessage("Errore: tipo file non valido! Tipi file validi: jpg | png | gif | svg | tif | bmp | jpeg");
			throw new ValidatorException(message);
		  }
 
		// 3. validate file size (should not be greater than 2048 bytes)
		if (part.getSize() > 1024*1024) {
			FacesMessage message = new FacesMessage("Errore: dimensione del file eccessiva! Massima dimensione: 1Mb/2Mb");
			throw new ValidatorException(message);
		}
	}
 
	// Extract file name from content-disposition header of file part
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		System.out.println("----- validator partHeader: " + partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return "";
	}
}