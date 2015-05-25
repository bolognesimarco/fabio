package com.bolo.photo.web.entity;

public enum Esperienza {
	Nessuna ("Nessuna"),
	Poca ("Poca"),
	Amatore ("Amatore"),
	Semipro ("Semi-pro"),
	Professionista ("Professionista");
	
	private final String esperienza;

	Esperienza (String esperienza){
		this.esperienza = esperienza;
	}
	
	public String getEsperienza() {
		return esperienza;
	}
	

}