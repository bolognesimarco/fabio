package com.bolo.photo.web.entity;

public enum Sesso {
	Uomo ("Uomo"),
	Donna ("Donna"),
	Altro ("Altro"),
	Societ� ("Societ�");
	
	private final String sesso;
	
	Sesso(String sesso){
		this.sesso = sesso;
	}
	
	public String getSesso(){
		return sesso;
	}
}
