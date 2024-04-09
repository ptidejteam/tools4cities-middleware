package ca.concordia.ngci.toolsforcitiesmiddleware.middleware;

import java.io.IOException;

import ca.concordia.ngci.toolsforcitiesmiddleware.consumer.IConsumer;
import ca.concordia.ngci.toolsforcitiesmiddleware.consumer.SimpleConsumer;
import ca.concordia.ngci.toolsforcitiesmiddleware.producer.IProducer;
import ca.concordia.ngci.toolsforcitiesmiddleware.producer.CSVProducer;

public class Main {
    public static void main(String[] args) throws IOException {
        IProducer producer = new CSVProducer();
        IConsumer consumer = new SimpleConsumer();
        

        Middleware middleware = new Middleware(producer, consumer);

        // Initiate request for data
        middleware.requestData();
    }
}
