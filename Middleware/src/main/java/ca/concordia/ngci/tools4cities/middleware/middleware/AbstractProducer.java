package ca.concordia.ngci.tools4cities.middleware.middleware;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is an abstract producer implementation containing fetch operation which can be used by any producer.
 * It also implements the Observer pattern to asynchronously notify consumers that data is ready to be consumed.
 */
public abstract class AbstractProducer<E> implements IProducer<E> {
	protected String filePath;
	protected RequestOptions fileOptions;
	private final List<IConsumer<?>> listOfConsumers = new ArrayList<>();

	@Override
	public final void addObserver(final IConsumer<E> aConsumer) {
		this.listOfConsumers.add(aConsumer);
	}

	@Override
	public final void notifyObservers(final List<E> results) {
		try {
			for (final Iterator<IConsumer<?>> iterator = this.listOfConsumers.iterator(); iterator.hasNext();) {

				final IConsumer<?> consumer = iterator.next();
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
		URI endpointURI = new URI(this.filePath);
		HttpClient client = HttpClient.newHttpClient();

        if ("POST".equalsIgnoreCase(fileOptions.method) ) {
            request = HttpRequest.newBuilder()
                    .uri(endpointURI)
                    .POST(BodyPublishers.ofString(this.fileOptions.requestBody))
                    .header("Content-Type", this.fileOptions.contentType)
                    .build();
        } else if ("GET".equalsIgnoreCase(this.fileOptions.method)) {
            request = HttpRequest.newBuilder()
                    .uri(endpointURI)
                    .GET()
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported method: " + this.fileOptions.method);
        }

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
