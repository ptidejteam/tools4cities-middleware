package ca.concordia.ngci.tools4cities.middleware.producers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * This producer can load CSV from a file or remotely via an HTTP request.
 */
public class CSVProducer extends AbstractProducer<String> implements IProducer<String> {

	public CSVProducer(String filePath, RequestOptions fileOptions) {
		this.filePath = filePath;
		this.fileOptions = fileOptions;
	}

	@Override
	public void fetchData() throws Exception {
		final String csvString = this.fetchFromPath();
		
		// split CSV string by line, add lines to the list
		final List<String> csvLines = new ArrayList<String>();
		csvLines.addAll(Arrays.asList(csvString.split(System.lineSeparator())));
		
		this.notifyObservers(csvLines);
	}
}
