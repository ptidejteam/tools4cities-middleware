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
        final IProducer<String> producer1 = new ThreeCsvProducer("/home/zongo/Documents/ConcordiaLabs/Workspace/Tools4Cities-Middleware/Conso_Moy_RTA_SCIAN_CI.csv", null, 3);
        final IProducer<String> producer2 = new ThreeCsvProducer("/home/zongo/Documents/ConcordiaLabs/Workspace/Tools4Cities-Middleware/Conso_Moy_RTA_SCIAN_Energir_RES.csv", null, 3);
        final IProducer<String> producer3 = new ThreeCsvProducer("/home/zongo/Documents/ConcordiaLabs/Workspace/Tools4Cities-Middleware/DonneesMTL_Short.csv", null, 3);

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

        // Testing with the actual expected sum for data extracted from the 4th line of the 4th column of each csv file
        Double expectedSum = 668.0; 
        
        Assertions.assertEquals(expectedSum, consumer.getSum());
        //System.out.print(consumer.getSum());
        
    }    
    
}