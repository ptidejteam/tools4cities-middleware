package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;

public class EnergyConsumptionProducer extends AbstractProducer<String> implements IProducer<String> {

	private final int columnIndex;

    public EnergyConsumptionProducer(String filePath, RequestOptions fileOptions, int columnIndex) {
        this.filePath = filePath;
        this.fileOptions = fileOptions;
        this.columnIndex = columnIndex;
    }

    public EnergyConsumptionProducer(String filePath, int columnIndex) {
    	this(filePath, null, columnIndex);
    }

    @Override
    public void fetchData() throws Exception {
        System.out.println("Fetching data from: " + filePath);

        try {
            final String csvString = this.fetchFromPath();
            System.out.println("Read CSV data: " + csvString);

            final List<String> csvLines = parseCsvManually(csvString);
            this.notifyObservers(csvLines); // Notify observers with raw string data
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
	
    private List<String> parseCsvManually(String csvString) {
        List<String> columnValues = new ArrayList<>();

        String[] lines = csvString.split("\\R");  // Matches any line separator

        if (lines.length > 3) {
            String[] values = lines[3].split(","); // Line index 3 corresponds to the 4th line (0-based index)
            
            if (values.length > columnIndex) {
                columnValues.add(values[columnIndex].trim());
            }
        }

        return columnValues; 
    }


        
}
