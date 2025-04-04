package ca.concordia.encs.citydata.core.utils;

import java.util.HashMap;

/**
 * This class represents HTTP request options, such as headers. It is used by
 * the AbstractProducer to fetch files via HTTP.
 * 
 * @author Gabriel C. Ullmann
 * @date 2025-03-28
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