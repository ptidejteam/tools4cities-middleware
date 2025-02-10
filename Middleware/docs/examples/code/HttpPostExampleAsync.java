import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostExampleAsync {
	
	public static void main(String[] args) throws IOException {
		String requestUrl = "http://localhost:8082/apply/sync";
		String jsonInput = "{\r\n"
				+ "  \"use\": \"ca.concordia.encs.citydata.producers.RandomNumberProducer\",\r\n"
				+ "  \"withParams\": [\r\n"
				+ "    {\r\n"
				+ "      \"name\": \"listSize\",\r\n"
				+ "      \"value\": 10\r\n"
				+ "    }\r\n"
				+ "  ],\r\n"
				+ "  \"apply\": [\r\n"
				+ "    {\r\n"
				+ "      \"name\": \"ca.concordia.encs.citydata.operations.AverageOperation\",\r\n"
				+ "      \"withParams\": [\r\n"
				+ "        {\r\n"
				+ "          \"name\": \"roundingMethod\",\r\n"
				+ "          \"value\": \"floor\"\r\n"
				+ "        }\r\n"
				+ "      ]\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"name\": \"ca.concordia.encs.citydata.operations.MergeOperation\",\r\n"
				+ "      \"withParams\": [\r\n"
				+ "        {\r\n"
				+ "          \"name\": \"targetProducer\",\r\n"
				+ "          \"value\": \"ca.concordia.encs.citydata.producers.RandomNumberProducer\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "          \"name\": \"targetProducerParams\",\r\n"
				+ "          \"value\": [\r\n"
				+ "            {\r\n"
				+ "              \"name\": \"listSize\",\r\n"
				+ "              \"value\": 4\r\n"
				+ "            }\r\n"
				+ "          ]\r\n"
				+ "        }\r\n"
				+ "      ]\r\n"
				+ "    }\r\n"
				+ "  ]\r\n"
				+ "}"; // Example JSON input

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