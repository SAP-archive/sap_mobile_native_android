package com.sap.dcode.agency.services;

public class Collections {
	
	public static final String TRAVEL_AGENCY_COLLECTION = "TravelAgencies_DQ";
	public static final String TRAVEL_AGENCY_ENTITY_TYPE = "RMTSAMPLEFLIGHT.Travelagency_DQ";
	public static final String TRAVEL_AGENCY_ENTRY_NAME = "NAME";
	public static final String TRAVEL_AGENCY_ENTRY_STREET = "STREET";
	public static final String TRAVEL_AGENCY_ENTRY_POSTBOX = "POSTBOX";
	public static final String TRAVEL_AGENCY_ENTRY_POSTAL_CODE = "POSTCODE";
	public static final String TRAVEL_AGENCY_ENTRY_CITY = "CITY";
	public static final String TRAVEL_AGENCY_ENTRY_COUNTRY = "COUNTRY";
	public static final String TRAVEL_AGENCY_ENTRY_REGION = "REGION";
	public static final String TRAVEL_AGENCY_ENTRY_TELEPHONE = "TELEPHONE";
	public static final String TRAVEL_AGENCY_ENTRY_URL = "URL";
	public static final String TRAVEL_AGENCY_ENTRY_LANGUAGE = "LANGU";
	public static final String TRAVEL_AGENCY_ENTRY_CURRENCY = "CURRENCY";
	public static final String TRAVEL_AGENCY_ENTRY_MIMETYPE = "mimeType";
	public static final String TRAVEL_AGENCY_ENTRY_ID = "agencynum";
	
	public static final String CARRIER_COLLECTION = "CarrierCollection";
	public static final String CARRIER_ENTRY_ID = "carrid";
	public static final String CARRIER_ENTRY_NAME = "CARRNAME";
	public static final String CARRIER_ENTRY_URL = "URL";
	
	public static String getEditResourcePath(String collection, String key){
		return new String(collection + "('"+ key + "')");
	}

}
