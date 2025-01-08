package com.concordia.middleware.producers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.concordia.middleware.core.AbstractProducer;
import com.concordia.middleware.core.IProducer;
import com.concordia.middleware.core.RequestOptions;

public class CSVProducer extends AbstractProducer<String> implements IProducer<String> {
	
	public CSVProducer(String filePath, RequestOptions fileOptions) {
		this.filePath = filePath;
		this.fileOptions = fileOptions;
	}

	@Override
	public void fetchData() throws Exception {
		try {
			final String csvString = new String(this.fetchFromPath());
			
			// split CSV string by line, add lines to the list
			final List<String> csvLines = new ArrayList<String>();
			csvLines.addAll(Arrays.asList(csvString.split(System.lineSeparator())));
			
			this.notifyObservers(csvLines);
		} catch (IOException e) {
			throw new RuntimeException("Error reading CSV file", e);
		}
		
	}
	
	@Override
    protected byte[] fetchFromPath() throws Exception {
        // Read the content of the file into a string
        return Files.readAllBytes(Paths.get(filePath));
    }
}
