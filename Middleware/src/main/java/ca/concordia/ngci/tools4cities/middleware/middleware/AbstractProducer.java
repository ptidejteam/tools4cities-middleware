package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;

/**
 * This is an abstract producer implementation containing fetch operation which
 * can be used by any producer. It also implements the Observer pattern to
 * asynchronously notify consumers that data is ready to be consumed.
 */
public abstract class AbstractProducer<E> implements IProducer<E> {
	protected String filePath;
	protected RequestOptions fileOptions;
	private final List<IConsumer<E>> listOfConsumers = new ArrayList<>();

	@Override
	public final void addObserver(final IConsumer<E> aConsumer) {
		this.listOfConsumers.add(aConsumer);
	}

	@Override
	public final void notifyObservers(final List<E> results) {
		try {
			for (final Iterator<IConsumer<E>> iterator = this.listOfConsumers.iterator(); iterator.hasNext();) {

				final IConsumer<E> consumer = iterator.next();
				consumer.newDataAvailable(results);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetch file via HTTP or filesystem depending on whether it is a path or URL
	 */
	protected String fetchFromPath() throws Exception {
		if (this.filePath.startsWith("http")) {
			return this.doHTTPRequest();
		}
		return this.readFile();
	}

	/**
	 * Fetch file via HTTP GET or POST
	 */
	protected String doHTTPRequest() throws Exception {
		HttpRequest request;
		BodyPublisher requestBody;
		URI endpointURI = new URI(this.filePath);
		Builder requestBuilder = HttpRequest.newBuilder().uri(endpointURI);
		HttpClient client = HttpClient.newHttpClient();

		// TODO: we should support idempotent HTTP methods only to avoid unexpected side effects (e.g. a producer changing data in the API)
		// for now, I kept support to PUT and POST because they are needed for Hub API auth
		switch (this.fileOptions.method) {
		case "HEAD":
			requestBuilder.HEAD();
			break;
		case "GET":
			requestBuilder.GET();
			break;
		case "POST":
			requestBody = BodyPublishers.ofString(this.fileOptions.requestBody);
			requestBuilder.POST(requestBody);
			break;
		case "PUT":
			requestBody = BodyPublishers.ofString(this.fileOptions.requestBody);
			requestBuilder.PUT(requestBody);
			break;
		default:
			throw new IllegalArgumentException("Unsupported method: " + this.fileOptions.method);
		}

		// add headers to builder, if any
		if (this.fileOptions.headers != null && !this.fileOptions.headers.isEmpty()) {
			for (Entry<String, String> header : this.fileOptions.headers.entrySet()) {
				requestBuilder.header(header.getKey(), header.getValue());
			}
		}
		System.out.println(this.fileOptions.headers);
		request = requestBuilder.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.statusCode());
		
		if (this.fileOptions.returnHeaders) {
			Gson gson = new Gson();
			return gson.toJson(response.headers().map());
		}
		return response.body();
	}

	/**
	 * Fetch file from filesystem
	 */
	protected String readFile() throws Exception {
		Path path = Paths.get(this.filePath);
		return new String(Files.readAllBytes(path));
	}

}
