package com.bolo.photo.web.entity;


public enum RegioneItaliana {
	ABRUZZo	("ABRUZZo"),
	BASILICATAa	("BASILICATAa"),
	CALABRIa	("CALABRIa"),
	CAMPANIa ("CAMPANIa"),
	EMILIa_ROMAGNa	("EMILIa_ROMAGNa"),
	FRIULi_VENEZIa_GIULIa	("FRIULi_VENEZIa_GIULIa"),
	LAZIo	("LAZIo"),
	LIGURIa	("LIGURIa"),
	LOMBARDIa	("LOMBARDIa"),
	MARCHe	("MARCHe"),
	MOLISe	("MOLISe"),
	PIEMONTe	("PIEMONTe"),
	PUGLIa	("PUGLIa"),
	SARDEGNa	("SARDEGNa"),
	SICILIa ("SICILIa"),
	TOSCANa	("TOSCANa"),
	TRENTINo_ALTo_ADIGe ("TRENTINo_ALTo_ADIGe"),
	UMBRIa	("UMBRIa"),
	VAl_dAOSTa	("VAl_dAOSTa"),
	VENETo	("VENETo");
	
	private final String regioneitaliana;
	
	
	RegioneItaliana(String regioneitaliana){
		this.regioneitaliana = regioneitaliana;
	}

	public String getRegioneitaliana() {
		return regioneitaliana;
	}
	
	public static String valueOfOrDefault(String myValue) {
		//replace space with underscore so it matches enum name
		        String value=myValue.toUpperCase().replaceAll("\\s", "_");
		        for(RegioneItaliana type : RegioneItaliana.class.getEnumConstants()) {
		          if(type.name().equalsIgnoreCase(value)) {
		            return type.toString();
		          }
		        }
		        return myValue;
		      }
	
}
