package com.bolo.photoshooters;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class GoogleMaps {

	public static void main(String[] aa) throws Exception{
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAg7ZoORXv7d2eMGKb-pB7_QZReIPiIzxw");
		
		LatLng latlng = new LatLng(45.403385, 9.492744);
		//GeocodingResult[] results =  GeocodingApi.reverseGeocode(context, latlng).await();
		GeocodingResult[] results =  GeocodingApi.geocode(context,"cere").await();
		
		for (GeocodingResult geocodingResult : results) {
			System.out.println(geocodingResult.formattedAddress);	
		}
		
		
	}
}
