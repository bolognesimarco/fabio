package com.bolo.photo.web.entity;


public enum RegioneItaliana {
	Abruzzo	("Abruzzo"),
	Basilicata	("Basilicata"),
	Calabria	("Calabria"),
	Campania	("Campania"),
	Emilia_Romagna	("Emilia_Romagna"),
	Friuli_Venezia_Giulia	("Friuli_Venezia_Giulia"),
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
	Trentino_Alto_Adige	("Trentino_Alto_Adige"),
	Umbria	("Umbria"),
	Valle_Aosta	("Valle_Aosta"),
	Veneto	("Veneto");
	
	private final String regioneitaliana;
	
	
	RegioneItaliana(String regioneitaliana){
		this.regioneitaliana = regioneitaliana;
	}

	public String getRegioneitaliana() {
		return regioneitaliana;
	}
	
}
