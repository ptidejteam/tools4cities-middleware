package ca.concordia.encs.citydata.core;

import java.util.HashMap;

/**
 * This class represents HTTP request options. Only basic options have been
 * included so far, it will be further detailed in the future.
 */
public class RequestOptions {
	public String method;
	public String requestBody = "";
	public Boolean returnHeaders = false;
	public HashMap<String, String> headers = new HashMap<String, String>();

	public void addToHeaders(String key, String value) {
		headers.put(key, value);
	}

}