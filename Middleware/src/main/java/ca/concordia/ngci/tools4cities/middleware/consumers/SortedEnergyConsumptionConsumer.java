package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.*;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

// This class computes the total energy consumption of a specified region in a city, collected for 4 months (january-April) and sorts these total consumption ascending
public class SortedEnergyConsumptionConsumer extends AbstractConsumer<String> implements IConsumer<String> {
    private List<String> results;
    private Map<Integer, Double> monthlyConsumption;

    public SortedEnergyConsumptionConsumer(Set<IProducer<String>> producers) {
        super(producers);
        this.results = new ArrayList<>();
        this.monthlyConsumption = new HashMap<>();
    }

    @Override
    public List<String> getResults() {
        return results;
    }

    @Override
    public void newDataAvailable(List<String> data) {
    	if (data == null || data.isEmpty()) {
            System.err.println("Received null or empty data!");
            return;
        }

        // Process the incoming data
        processData(data);
    }

    private void processData(List<String> data) {
    	// Skip header by starting from the second line if the first line is a header
        boolean isHeader = data.get(0).contains("kWh"); // Adjust this based on header content
        int startIndex = isHeader ? 1 : 0;

        for (int i = startIndex; i < data.size(); i++) {
            String line = data.get(i);
            String[] values = line.split(",");

            // Check for valid line length
            if (values.length < 4) {
                System.err.println("Invalid line: " + line);
                continue;
            }

            String date = values[2];
            double kWh;
            
            // Parse kWh value
            try {
                kWh = Double.parseDouble(values[3]);
            } catch (NumberFormatException e) {
                continue;
            }

            // Extract the month from the date (assuming format YYYY-MM-DD)
            int month = Integer.parseInt(date.substring(5, 7));

            // Sum the kWh values for each month
            monthlyConsumption.put(month, monthlyConsumption.getOrDefault(month, 0.0) + kWh);
        }
    }

    public Map<Integer, Double> getMonthlyConsumption() {
        return new HashMap<>(monthlyConsumption); 
    }

    public void printSortedMonthlyConsumption() {
        // Create a sorted list of the entries based on the monthly consumption
        List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(monthlyConsumption.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue()); 

        // Print the sorted monthly consumption
        System.out.println("Monthly KWh Consumption (sorted):");
        for (Map.Entry<Integer, Double> entry : sortedEntries) {
            System.out.printf("Month: %d, Total Consumption: %.2f kWh%n", entry.getKey(), entry.getValue());
        }
    }
}
