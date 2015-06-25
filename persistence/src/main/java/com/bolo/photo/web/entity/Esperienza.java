package com.bolo.photo.web.entity;

public enum Esperienza {
	NESSUNa ("NESSUNa"),
	POCa ("POCa"),
	AMATORe ("AMATORe"),
	SEMi_PRo ("SEMi_PRo"),
	PROFESSIONISTa ("PROFESSIONISTa");
	
	private final String esperienza;

	Esperienza (String esperienza){
		this.esperienza = esperienza;
	}
	
	public String getEsperienza() {
		return esperienza;
	}
	

}