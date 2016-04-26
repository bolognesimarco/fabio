package com.bolo.photoshooters.util;

import java.util.Comparator;

import com.bolo.photo.web.entity.Annuncio;
import com.bolo.photo.web.entity.Messaggio;

public class AnnunciComparator implements Comparator<Annuncio> {

	public int compare(Annuncio u1, Annuncio u2) {
		int c = u2.getRisposte().get(0).getMessaggi().get(0).getData().compareTo(u1.getRisposte().get(0).getMessaggi().get(0).getData());
//		System.out.println("comparing u2 "+u2.getData()+" and u1 "+u1.getData()+" : "+c);
		return c;
	}

	
}
