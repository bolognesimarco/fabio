package com.bolo.photoshooters.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.persistence.indirection.IndirectList;

public class IndirectListSorter<T> {
	public void sortIndirectList(List<T> toSort, Comparator<T> c){
		
		if(toSort instanceof IndirectList){	
			Collections.sort((List<T>) ((IndirectList)toSort).getDelegateObject(), c);
		}else{
			Collections.sort(toSort, c);
		}
	}
}
