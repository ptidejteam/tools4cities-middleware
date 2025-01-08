package com.concordia;

import com.google.gson.JsonArray;
import com.concordia.middleware.consumers.BuildingConsumer;
import com.concordia.middleware.core.IConsumer;
import com.concordia.middleware.core.IOperation;
import com.concordia.middleware.core.IProducer;
import com.concordia.middleware.operations.FilterOperation;
import com.concordia.middleware.operations.FilterRangeOperation;
import com.concordia.middleware.producers.EnergyConsumptionProducer;
import com.concordia.middleware.producers.GeometryProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/consumers/energyConsumption")
public class EnergyConsumptionController {

    // Helper method to create producers
    private Set<IProducer<String>> getProducers() {
        final Set<IProducer<String>> producers = new HashSet<>();
        IProducer<String> energyConsumptionProducer = new EnergyConsumptionProducer("montreal");
        IProducer<String> geometryProducer = new GeometryProducer("montreal");
        producers.add(energyConsumptionProducer);
        producers.add(geometryProducer);
        return producers;
    }

    // Helper method to create operations
    private Set<IOperation> getOperations(String startDatetime, String endDatetime, String postalCode) {
        final Set<IOperation> operations = new HashSet<>();
        operations.add(new FilterRangeOperation(startDatetime, endDatetime));
        operations.add(new FilterOperation(postalCode, false));
        return operations;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(value = "startDatetime") String startDatetime,
            @RequestParam(value = "endDatetime") String endDatetime,
            @RequestParam(value = "postalCode") String postalCode) {

        JsonArray resultArray = new JsonArray();

        try {
            Set<IProducer<String>> producers = getProducers();
            Set<IOperation> operations = getOperations(startDatetime, endDatetime, postalCode);
            IConsumer<String> buildingConsumer = new BuildingConsumer(producers, operations);

            return buildingConsumer.getResults().get(0).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultArray.toString();
    }
}