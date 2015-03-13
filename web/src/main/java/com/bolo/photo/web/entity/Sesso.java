package com.bolo.photo.web.entity;

public enum Sesso {
	M ("M"),
	F ("F");
	
	private final String sesso;
	
	Sesso(String sesso){
		this.sesso = sesso;
	}
	
	public String getSesso(){return sesso;}
}
