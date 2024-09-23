package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ca.concordia.ngci.tools4cities.middleware.producers.ThreeCsvProducer;
import ca.concordia.ngci.tools4cities.middleware.consumers.ThreeCsvSumConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class TestThreeCsvSum {

    @Test
    public void testThreeCsvSum() {
        // Create producers for three CSV files, each extracting values from the 4th column
        final IProducer<String> producer1 = new ThreeCsvProducer("./src/main/resources/Conso1.csv", null, 3);
        final IProducer<String> producer2 = new ThreeCsvProducer("./src/main/resources/Conso2.csv"", null, 3);
        final IProducer<String> producer3 = new ThreeCsvProducer("./src/main/resources/Conso3.csv"", null, 3);

        final Set<IProducer<String>> producers = new HashSet<>();
        producers.add(producer1);
        producers.add(producer2);
        producers.add(producer3);

        // Create the consumer
        final ThreeCsvSumConsumer consumer = new ThreeCsvSumConsumer(producers);

        // Trigger data fetch
        try {
            producer1.fetchData();
            producer2.fetchData();
            producer3.fetchData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Double expectedSum = 668.0; 
        
        Assertions.assertEquals(expectedSum, consumer.getSum());
        
    }    
    
}
