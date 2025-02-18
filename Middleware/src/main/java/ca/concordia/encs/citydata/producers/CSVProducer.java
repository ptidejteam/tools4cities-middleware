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

	private String filePath;
    private RequestOptions fileOptions;

    public CSVProducer(String filePath, RequestOptions fileOptions) {
        this.filePath = filePath;
        this.fileOptions = fileOptions;
    }

    @Override
    public void fetch() {
        final String csvString = new String(this.fetchFromPath());
        // split CSV string by line, add lines to the list
        final ArrayList<String> csvLines = new ArrayList<>();
        csvLines.addAll(Arrays.asList(csvString.split(System.lineSeparator())));
        this.result = csvLines;
        this.applyOperation();
    }

    @Override
    public boolean matchesQuery(String query) {
        // If no query provided, consider it a match
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        
        // If we have any CSV lines in the result, search through them
        if (result != null && !result.isEmpty()) {
            return result.stream()
                .anyMatch(line -> line.toLowerCase()
                    .contains(query.toLowerCase()));
        }
        
        // If no results yet, check the file path
        return filePath != null && filePath.toLowerCase()
            .contains(query.toLowerCase());
    }

}