package com.bolo.photoshooters.web;
 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.event.map.OverlaySelectEvent;
 
@ManagedBean
public class MapBean {
     
	private MapModel model;
	public MapBean() {
	model = new DefaultMapModel();
	//add markers
	}
	public MapModel getModel() {
	return model;
	}
	public void onMarkerSelect(OverlaySelectEvent event) {
	Marker selectedMarker = (Marker) event.getOverlay();
	//add facesmessage
	String msg = "ciao mappa";
	FacesMessage messaggio = new FacesMessage(msg);
	} 
}
