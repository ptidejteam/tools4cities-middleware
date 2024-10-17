package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;

public class PeriodEnergyConsumptionProducer extends AbstractProducer<String> implements IProducer<String> {

	    private final int startMonthIndex;  // For example, 1 for January (1-based index)
	    private final int endMonthIndex;    // For example, 3 for March (1-based index)

	    public PeriodEnergyConsumptionProducer(String filePath, RequestOptions fileOptions, int startMonthIndex, int endMonthIndex) {
	        this.filePath = filePath;
	        this.fileOptions = fileOptions;
	        this.startMonthIndex = startMonthIndex;
	        this.endMonthIndex = endMonthIndex;
	    }
	    
	    public PeriodEnergyConsumptionProducer(String filePath, int startMonthIndex, int endMonthIndex) {
	        this(filePath, null, startMonthIndex, endMonthIndex);
	    }

	    @Override
	    public void fetchData() throws Exception {
	        
	        try {
	        	final String csvString = this.fetchFromPath();
	        	System.out.println("Read CSV data: " + csvString);
	            final List<String> csvLines = parseCsvManually(csvString);
	            
	            this.notifyObservers(csvLines); // To notify observers with the filtered data
	            
	        } catch (IOException e) {
	            throw new RuntimeException("Error reading CSV file", e);
	        }
	    }
	    /**
	     * Manually parse the CSV string and extract energy consumption values for the specified time period.
	     */
	   private List<String> parseCsvManually(String csvString) {
		   List<String> filteredValues = new ArrayList<>();
		    String[] lines = csvString.split("\\R");

		    for (int i = 1; i < lines.length; i++) {
		        String[] values = lines[i].split(",");
		        String postalCode = values[1];

		        if (postalCode.equals("H1A")) {
		            // Get January's consumption
		            if (values.length > 4) { // Ensure there are enough columns to be able to fetch data from the third column of my file
		                double januaryConsumption = Double.parseDouble(values[4].trim());
		                filteredValues.add(postalCode + ":1:" + januaryConsumption); // Use "1" for January
		            }

		            // Get March's consumption
		            if (values.length > 6) { // Ensure there are enough columns to be able to fetch data from the fifth column of my file
		                double marchConsumption = Double.parseDouble(values[6].trim());
		                filteredValues.add(postalCode + ":3:" + marchConsumption); // Use "3" for March
		            }
		        }
		    }
		    return filteredValues;
	   }
}
