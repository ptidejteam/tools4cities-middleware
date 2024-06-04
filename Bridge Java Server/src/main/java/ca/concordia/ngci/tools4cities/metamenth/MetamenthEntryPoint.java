package ca.concordia.ngci.tools4cities.metamenth;


import java.util.HashMap;
import java.util.Map;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.IPythonEntryPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IBinaryMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IRoom;
import py4j.GatewayServer;
import py4j.JavaServer;
import py4j.Py4JException;
import py4j.Py4JNetworkException;

public class MetamenthEntryPoint {
    
    private MetamenthRepository repository;

    public MetamenthEntryPoint(){
        repository = new MetamenthRepository();
    }



    public MetamenthRepository getMetamenthRepository(){
        return repository;
    }


    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(new MetamenthEntryPoint());
        gatewayServer.start();
       
        
  }
}
