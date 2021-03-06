package com.bolo.photoshooters.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.bolo.photo.web.entity.TipoUtente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;

@ManagedBean
@SessionScoped
public class ParametersBean {
	
	private ServiziComuni serv = new ServiziComuniImpl();
	
	private Map<String, Object> tipiUtente;
	private Map<String, Object> fasciaEt� = new LinkedHashMap<String,Object>();

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
	

	public void rangeFill(List<Integer> campo2, int start, int end) {
	    for (int i = start; i <= end; i++) {
	    	campo2.add(new Integer(new Integer(i)));
	    }
	} 
	
	public void fillSelectItems() {
		rangeFianchi.clear();
		rangeVita.clear();
		rangeAltezza.clear();
		rangePeso.clear();
		rangeSeno.clear();
		rangeTaglia.clear();
		rangeScarpe.clear();
		rangeTatuaggi.clear();
		rangePiercing.clear();
		rangeVoto.clear();
		rangeFill(rangeFianchi, 55, 160);
		rangeFill(rangeVita, 45, 130);
		rangeFill(rangeAltezza, 55, 160);
		rangeFill(rangePeso, 55, 160);
		rangeFill(rangeSeno, 55, 160);
		rangeFill(rangeTaglia, 30, 60);
		rangeFill(rangeScarpe, 30, 50);
		rangeFill(rangeTatuaggi, 0, 10);
		rangeFill(rangePiercing, 0, 10);
		rangeFill(rangeVoto, 0, 10);
	}
		
	private List<Integer> rangeFianchi = new ArrayList<Integer>();
	private List<Integer> rangeVita = new ArrayList<Integer>();
	private List<Integer> rangeAltezza = new ArrayList<Integer>();
	private List<Integer> rangePeso = new ArrayList<Integer>();
	private List<Integer> rangeSeno = new ArrayList<Integer>();
	private List<Integer> rangeTaglia = new ArrayList<Integer>();
	private List<Integer> rangeScarpe = new ArrayList<Integer>();
	private List<Integer> rangeTatuaggi = new ArrayList<Integer>();
	private List<Integer> rangePiercing = new ArrayList<Integer>();
	private List<Integer> rangeEt� = new ArrayList<Integer>();
	private List<Integer> rangeVoto = new ArrayList<Integer>();
	
	public void fillEt�(){
		fasciaEt�.put("<18",1);
		fasciaEt�.put("18-21",2);
		fasciaEt�.put("22-25",3);
		fasciaEt�.put("26-30",4);
		fasciaEt�.put("31-40",5);
		fasciaEt�.put(">40",6);
	}

	
	
//	**************GETTER&SETTERS********
	
	public List<Integer> getRangeEt�() {
		return rangeEt�;
	}

	public List<Integer> getRangeVoto() {
		return rangeVoto;
	}

	public void setRangeVoto(List<Integer> rangeVoto) {
		this.rangeVoto = rangeVoto;
	}


	public Map<String, Object> getFasciaEt�() {
		return fasciaEt�;
	}

	public void setFasciaEt�(Map<String, Object> fasciaEt�) {
		this.fasciaEt� = fasciaEt�;
	}

	public void setRangeEt�(List<Integer> rangeEt�) {
		this.rangeEt� = rangeEt�;
	}

	public List<Integer> getRangeTatuaggi() {
		return rangeTatuaggi;
	}

	public void setRangeTatuaggi(List<Integer> rangeTatuaggi) {
		this.rangeTatuaggi = rangeTatuaggi;
	}

	public List<Integer> getRangePiercing() {
		return rangePiercing;
	}

	public void setRangePiercing(List<Integer> rangePiercing) {
		this.rangePiercing = rangePiercing;
	}

	public List<Integer> getRangeFianchi() {
		return rangeFianchi;
	}

	public void setRangeFianchi(List<Integer> rangeFianchi) {
		this.rangeFianchi = rangeFianchi;
	}

	public List<Integer> getRangeVita() {
		return rangeVita;
	}

	public void setRangeVita(List<Integer> rangeVita) {
		this.rangeVita = rangeVita;
	}

	public List<Integer> getRangeAltezza() {
		return rangeAltezza;
	}

	public void setRangeAltezza(List<Integer> rangeAltezza) {
		this.rangeAltezza = rangeAltezza;
	}

	public List<Integer> getRangePeso() {
		return rangePeso;
	}

	public void setRangePeso(List<Integer> rangePeso) {
		this.rangePeso = rangePeso;
	}

	public List<Integer> getRangeSeno() {
		return rangeSeno;
	}

	public void setRangeSeno(List<Integer> rangeSeno) {
		this.rangeSeno = rangeSeno;
	}

	public List<Integer> getRangeTaglia() {
		return rangeTaglia;
	}

	public void setRangeTaglia(List<Integer> rangeTaglia) {
		this.rangeTaglia = rangeTaglia;
	}

	public List<Integer> getRangeScarpe() {
		return rangeScarpe;
	}

	public void setRangeScarpe(List<Integer> rangeScarpe) {
		this.rangeScarpe = rangeScarpe;
	}
	
	
}
