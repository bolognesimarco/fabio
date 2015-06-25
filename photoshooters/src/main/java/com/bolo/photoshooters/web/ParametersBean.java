package com.bolo.photoshooters.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.bolo.photo.web.entity.Sesso;
import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;

@ManagedBean
@SessionScoped
public class ParametersBean {
	
	private ServiziComuni serv = new ServiziComuniImpl();
	
	private Map<String, Object> tipiUtente;

	public Map<String, Object> getTipiUtente(){
		if(tipiUtente==null){
			tipiUtente = new LinkedHashMap<String, Object>();
			
			try {
				List<TipoUtente> tipi = serv.getAll(TipoUtente.class);
				System.out.println("SIZE:"+tipi.size());
				for (TipoUtente tipoUtente : tipi) {
					System.out.println(tipoUtente.getDescrizione()+"-"+tipoUtente.getId());
					tipiUtente.put(tipoUtente.getDescrizione(), tipoUtente.getId());	
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		return tipiUtente;
	}
	
	
}
