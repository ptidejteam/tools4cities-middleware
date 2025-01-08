package ca.concordia.ngci.tools4cities.middleware.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import ca.concordia.ngci.tools4cities.middleware.producers.RandomNumberProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.IntegerConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TestRandomNumberStorage {

    private void saveToCsv(List<Integer> data, String filePath) {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            csvWriter.append("Generated Numbers\n");
            for (Integer num : data) {
                csvWriter.append(num.toString()).append("\n");
            }
            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void case1s1() {
        System.out.println("Scenario 1: Collect data as it is generated for a specified period of time");

        try {
            final int listSize = 10;
            final int generationDelay = 300;
            final IProducer<Integer> producer = new RandomNumberProducer(listSize, generationDelay);
            final Set<IProducer<Integer>> producers = new HashSet<>();
            producers.add(producer);

            // Starting producer and then waiting for data to be generated
            Thread.sleep(2000);

            final IntegerConsumer consumer = new IntegerConsumer(producers);
            List<Integer> randomNumbers = consumer.getResults();
            Assertions.assertNotEquals(0, randomNumbers.size());

            saveToCsv(randomNumbers, "./src/test/data/file1.csv");

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void case1s2() {
        System.out.println("Scenario 2: Collect data after a specified period of time");

        final int listSize = 10;
        final int generationDelay = 2000;
        final IProducer<Integer> producer = new RandomNumberProducer(listSize, generationDelay);
        final Set<IProducer<Integer>> producers = new HashSet<>();
        producers.add(producer);

        final IntegerConsumer consumer = new IntegerConsumer(producers);
        List<Integer> randomNumbers = consumer.getResults();
        Assertions.assertNotEquals(0, randomNumbers.size());
        
        saveToCsv(randomNumbers, "./src/test/data/file2.csv");
    }
}
