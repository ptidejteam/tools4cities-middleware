package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.RequestOptions;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/**
 * This producer can load CSV from a file or remotely via an HTTP request.
 */
public class DelayEnergyConsumptionProducer extends AbstractProducer<String> implements IProducer<String> {

	private final int columnIndex;
    private long fetchEndTime;
    private long fetchStartTime;
    private int fetchCallCount = 0; // Counter to track fetch calls
    private boolean isFetched = false; // Flag to track if data has been fetched

    public DelayEnergyConsumptionProducer(String filePath, RequestOptions fileOptions, int columnIndex) {
        this.filePath = filePath;
        this.fileOptions = fileOptions;
        this.columnIndex = columnIndex;
    }

    @Override
    public void fetchData() throws Exception {
    	if (isFetched) {
            System.out.println("Data has already been fetched; skipping fetchData call.");
            return; // Prevent multiple calls
        }

    	fetchCallCount++; // Increment the counter each time fetchData is called
        System.out.println("fetchData() called " + fetchCallCount + " times for: " + filePath);

        fetchStartTime = System.currentTimeMillis(); // Start timing the fetch
        System.out.println("Fetch started at: " + fetchStartTime + " ms");


        try {
            final String csvString = new String(this.fetchFromPath()); // Fetch the CSV data as a string
            if (csvString == null || csvString.isEmpty()) {
                System.err.println("Fetched CSV data is null or empty.");
                return; // Early return if the CSV string is null or empty
            }

            System.out.println("Read CSV data: " + csvString);

            final List<String> csvLines = parseCsvManually(csvString); // Parse the data into lines
            if (csvLines.isEmpty()) {
                System.err.println("Parsed CSV lines are empty.");
                return; // Early return if no lines are parsed
            }

            this.notifyObservers(csvLines); // Notify observers with parsed data
            System.out.println("Successfully notified observers.");

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);

        } finally {
            fetchEndTime = System.currentTimeMillis(); // End timing the fetch
            System.out.println("Fetch ended at: " + fetchEndTime + " ms");
        }
    }

    /**
     * Manually parse the CSV string and extract values from the specified column.
     */
    private List<String> parseCsvManually(String csvString) {
        List<String> columnValues = new ArrayList<>();

        // Split CSV content by line
        String[] lines = csvString.split("\\R");  // Matches any line separator

        // Ensure that the file has at least 3 lines
        if (lines.length > 2) {
            // Extract value from the 4th line
            String[] values = lines[2].split(","); // Line index 2 corresponds to the 3rd line (0-based index)
            
            // Safely get the value from the desired column index
            if (values.length > columnIndex) {
                columnValues.add(values[columnIndex].trim());
            }
        }

        return columnValues; // Return the string representation of the column values from the 3rd line
    }
    
    public long getFetchTime() {
        return fetchEndTime - fetchStartTime;
    }

    public int getFetchCallCount() {
        return fetchCallCount; // Return the number of times fetchData was called
    }   
    
}
