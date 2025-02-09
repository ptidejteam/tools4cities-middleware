import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDeleteExampleApply {
	
public static void main(String[] args) throws Exception {
	String runnerId = "example-id";
	String requestUrl = "http://localhost:8082/apply/delete/" + runnerId;

	URL url = new URI.toURL(requestUrl);
	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	connection.setRequestMethod("DELETE");

	int responseCode = connection.getResponseCode();
	System.out.println("DELETE Response Code: " + responseCode);
	}		
}