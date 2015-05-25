package com.bolo.photo.web.entity;


public enum RegioneItaliana {
	Abruzzo	("Abruzzo"),
	Basilicata	("Basilicata"),
	Calabria	("Calabria"),
	Campania	("Campania"),
	EmiliaRomagna	("Emilia Romagna"),
	FriuliVeneziaGiulia	("Friuli Venezia Giulia"),
	Lazio	("Lazio"),
	Liguria	("Liguria"),
	Lombardia	("Lombardia"),
	Marche	("Marche"),
	Molise	("Molise"),
	Piemonte	("Piemonte"),
	Puglia	("Puglia"),
	Sardegna	("Sardegna"),
	Sicilia	("Sicilia"),
	Toscana	("Toscana"),
	TrentinoAltoAdige	("Trentino Alto Adige"),
	Umbria	("Umbria"),
	ValAosta	("Val d'Aosta"),
	Veneto	("Veneto");
	
	private final String regioneitaliana;
	
	
	RegioneItaliana(String regioneitaliana){
		this.regioneitaliana = regioneitaliana;
	}

	public String getRegioneitaliana() {
		return regioneitaliana;
	}
	
}
