package com.bolo.photoshooters.util;

import java.util.Comparator;

import com.bolo.photo.web.entity.Messaggio;

public class MessaggiComparator implements Comparator<Messaggio> {
// l'array � ordinato in modo che get(0) � il pi� recente come Data!!
	public int compare(Messaggio u1, Messaggio u2) {
//		System.out.println("in MessaggiComparator: ");
		int c = u2.getData().compareTo(u1.getData());
//		System.out.println("comparing u2 "+u2.getData()+" and u1 "+u1.getData()+" : "+c);
		return c;
	}

	
}
