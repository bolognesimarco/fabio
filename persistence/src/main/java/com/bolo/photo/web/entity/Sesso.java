package com.bolo.photo.web.entity;

public enum Sesso {
	Uomo ("Uomo"),
	Donna ("Donna"),
	Altro ("Altro"),
	Società ("Società");
	
	private final String sesso;
	
	Sesso(String sesso){
		this.sesso = sesso;
	}
	
	public String getSesso(){
		return sesso;
	}
}
