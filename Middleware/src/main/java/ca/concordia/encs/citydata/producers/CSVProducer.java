package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.Arrays;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.RequestOptions;

/**
 * This producer can load CSV from a file or remotely via an HTTP request.
 */
public class CSVProducer extends AbstractProducer<String> implements IProducer<String> {

	public CSVProducer(String filePath, RequestOptions fileOptions) {
		this.filePath = filePath;
		this.fileOptions = fileOptions;
	}

	// I added the error handling to ensure I actually read my local file

	@Override
	public void fetch() {
		final String csvString = new String(this.fetchFromPath());

		// split CSV string by line, add lines to the list
		final ArrayList<String> csvLines = new ArrayList<String>();
		csvLines.addAll(Arrays.asList(csvString.split(System.lineSeparator())));
		this.result = csvLines;
		this.applyOperation();
	}

}