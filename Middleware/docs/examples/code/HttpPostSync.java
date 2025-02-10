import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostSync {
	
	public static void main(String[] args) throws IOException {
		String requestUrl = "http://localhost:8080/apply/sync";
		String jsonInput = "{\r\n"
				+ "    \"use\": \"ca.concordia.encs.citydata.producers.EnergyConsumptionProducer\",\r\n"
				+ "    \"withParams\": [\r\n"
				+ "        {\r\n"
				+ "            \"name\": \"city\",\r\n"
				+ "            \"value\": \"montreal\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"apply\": [\r\n"
				+ "        {\r\n"
				+ "            \"name\": \"ca.concordia.encs.citydata.operations.StringFilterOperation\",\r\n"
				+ "            \"withParams\": [\r\n"
				+ "                {\r\n"
				+ "                    \"name\": \"filterBy\",\r\n"
				+ "                    \"value\": \"09:45:00\"\r\n"
				+ "                }\r\n"
				+ "            ]\r\n"
				+ "        }\r\n"
				+ "    ]\r\n"
				+ "}";

		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInput.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		int responseCode = connection.getResponseCode();
		System.out.println("POST Response Code: " + responseCode);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println(response.toString());
		}
	}
}