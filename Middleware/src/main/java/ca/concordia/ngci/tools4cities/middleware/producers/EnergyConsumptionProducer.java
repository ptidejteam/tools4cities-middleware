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
            final List<String> csvLines = parseCsvManually(csvString);
            this.notifyObservers(csvLines);
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
   
    private List<String> parseCsvManually(String csvString) {
        List<String> columnValues = new ArrayList<>();

        String[] lines = csvString.split("\\R"); 

        if (lines.length > 3) {
          
            String[] values = lines[3].split(","); 
            
            if (values.length > columnIndex) {
                columnValues.add(values[columnIndex].trim());
            }
        }

        return columnValues; 
    }
        
}
