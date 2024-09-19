package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;

public class ThreeCsvProducer extends AbstractProducer<String> implements IProducer<String> {

	private final int columnIndex;
    //private boolean fileProcessed = false;	I added it for debugging purposes

    public ThreeCsvProducer(String filePath, RequestOptions fileOptions, int columnIndex) {
        this.filePath = filePath;
        this.fileOptions = fileOptions;
        this.columnIndex = columnIndex;
    }

    public ThreeCsvProducer(String filePath, int columnIndex) {
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
    
    

    /* @Override
    protected String fetchFromPath() throws Exception {
        System.out.println("Reading file from path: " + filePath);
        return new String(Files.readAllBytes(Paths.get(filePath)));
    } */
    
    /**
     * Manually parse the CSV string and extract values from the specified column.
     */
    private List<String> parseCsvManually(String csvString) {
        List<String> columnValues = new ArrayList<>();

        // Split CSV content by line
        String[] lines = csvString.split("\\R");  // Matches any line separator

        // Ensure that the file has at least 4 lines
        if (lines.length > 3) {
            // Extract value from the 4th line
            String[] values = lines[3].split(","); // Line index 3 corresponds to the 4th line (0-based index)
            
            // Safely get the value from the desired column index
            if (values.length > columnIndex) {
                columnValues.add(values[columnIndex].trim());
            }
        }

        return columnValues; // Return the string representation of the column values from the 4th line
    }


        
}