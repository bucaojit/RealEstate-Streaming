package com.dataeng;

import java.util.Map;

public class RESTInterface {
	String zillowRESTCode;
	String zillowRESTProperty = "zillow.rest.code";
	
	// Get Details, Provide the property details
	public Map<String, String> getDetailsForProperty(String property) {
		return null;
	}
	
	// Get Zillow Property ID (zpid)
	// Example: http://www.zillow.com/webservice/GetSearchResults.htm?zws-id=<ZWSID>&address=2114+Bigelow+Ave&citystatezip=Seattle%2C+WA
	private Map<String,String> getZPID(String address, String cityStateZip) {
		return null;
	}
	
	// GetUpdatedPropertyDetails API
	// Example: http://www.zillow.com/webservice/GetUpdatedPropertyDetails.htm?zws-id=<ZWSID>&zpid=48749425 
	// Details: https://www.zillow.com/howto/api/GetUpdatedPropertyDetails.htm
	private Map<String,String> getUpdatedDetails(Map<String,String> propertyDetails) {
		return null;
	}
	
}
