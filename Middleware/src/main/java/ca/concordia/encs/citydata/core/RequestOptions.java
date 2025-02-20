package ca.concordia.encs.citydata.core;

import java.util.HashMap;

/**
 * This class represents HTTP request options. Only basic options have been
 * included so far, it will be further detailed in the future. Last Update:
 * 2-19-2025
 */

public class RequestOptions {
	public String method;
	public final String requestBody = "";
	public final Boolean returnHeaders = false;
	public final HashMap<String, String> headers = new HashMap<String, String>();

	public void addToHeaders(String key, String value) {
		headers.put(key, value);
	}

}