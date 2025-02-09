import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPutExampleApply {

	public static void main(String[] args) throws Exception {
		String requestUrl = "http://localhost:8082/apply/update";
		String jsonInput = "{\"update\": \"newValue\"}";

		URL url = new URI.toURL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInput.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		int responseCode = connection.getResponseCode();
		System.out.println("PUT Response Code: " + responseCode);
	}
}