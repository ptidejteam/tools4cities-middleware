package ca.concordia.ngci.tools4cities.middleware.consumers;

import java.util.*;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

/* This class computes the total energy consumption of a specified region in a city, collected for 4 months (january-April) and sorts these total consumption ascending
    */
public class SortedEnergyConsumptionConsumer extends AbstractConsumer<String> implements IConsumer<String> {
    private List<String> results;
    private Map<Integer, Double> monthlyConsumption;

    public SortedEnergyConsumptionConsumer(Set<IProducer<String>> producers) {
        super(producers);
        this.results = new ArrayList<>();
        this.monthlyConsumption = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public List<String> getResults() {
        return results;
    }

    @Override
    public void newDataAvailable(List<String> data) {
        if (data == null || data.isEmpty()) {
            System.err.println("Received null or empty data!");
            return; // Exit early if data is null or empty
        }

        // Process the incoming data
        processData(data);
    }

    private synchronized void updateMonthlyConsumption(int month, double kWh) {
    	 // Initialize monthlyConsumption if it is null
        if (monthlyConsumption == null) {
            monthlyConsumption = Collections.synchronizedMap(new HashMap<>());
        }

        // Update the map safely
        monthlyConsumption.put(month, monthlyConsumption.getOrDefault(month, 0.0) + kWh);
    }

    private void processData(List<String> data) {
        // Skip header by starting from the second line if the first line is a header
        boolean isHeader = data.get(0).contains("kWh"); // Adjust to the actual header's content
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

            // Parse kWh value, with debugging for invalid entries
            try {
                kWh = Double.parseDouble(values[3]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid KWh value: " + line);
                continue;
            }

            // Extract the month from the date (assuming format YYYY-MM-DD)
            int month = Integer.parseInt(date.substring(5, 7));

            // Safely update the monthly consumption map
            updateMonthlyConsumption(month, kWh);
        }
    }

    public synchronized Map<Integer, Double> getMonthlyConsumption() {
        ic synchronized Map<Integer, Double> getMonthlyConsumption() {
    	if (monthlyConsumption == null) {
            return Collections.emptyMap(); // Return an empty map if no data is available
        }

        // Sort the map by value (ascending)
        return monthlyConsumption.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(LinkedHashMap::new, 
                     (map, entry) -> map.put(entry.getKey(), entry.getValue()), 
                     Map::putAll);
    }

    public synchronized void printSortedMonthlyConsumption() {
        // Create a sorted list of the entries based on the monthly consumption
        List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(monthlyConsumption.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue()); // Sort by kWh consumption

        // Print the sorted monthly consumption
        System.out.println("Monthly KWh Consumption (sorted):");
        for (Map.Entry<Integer, Double> entry : sortedEntries) {
            System.out.printf("Month: %d, Total Consumption: %.2f kWh%n", entry.getKey(), entry.getValue());
        }
    }
}
