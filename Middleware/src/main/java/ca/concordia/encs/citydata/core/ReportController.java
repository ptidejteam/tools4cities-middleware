package ca.concordia.encs.citydata.core;

import ca.concordia.encs.citydata.datastores.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
/***
 * This class has all the routes for the report generation
 * @Author: Rushin Makwana
 * @Date: 26/2/2024
 */
@RestController
@RequestMapping("/report")
public class ReportController {
@Autowired
    private MongoDataStore mongoDataStore;

    @RequestMapping(value = "/producers", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, String>>> report() {
        List<Map<String, String>> report = new ArrayList<>();
        List<ProducerCallInfo> callInfoList = mongoDataStore.findAll();
        Map<String, Integer> producerCountMap = new HashMap<>();
        Map<String, Date> lastRequestDateMap = new HashMap<>();

        for (ProducerCallInfo callInfo : callInfoList) {
            String producerName = callInfo.getProducerName();
            producerCountMap.put(producerName, producerCountMap.getOrDefault(producerName, 0) + 1);
            lastRequestDateMap.put(producerName, callInfo.getTimestamp());
        }

        producerCountMap.forEach((producerName, count) -> {
            Map<String, String> reportEntry = new HashMap<>();
            reportEntry.put("query", producerName);
            reportEntry.put("count", String.valueOf(count));
            reportEntry.put("lastRequestDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(lastRequestDateMap.get(producerName)));
            report.add(reportEntry);
        });

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }
    @RequestMapping(value = "/producers", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllProducers() {
        mongoDataStore.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("All producer records have been deleted.");
    }
}