package com.middleware.metamenth;


import java.util.HashMap;
import java.util.Map;

import com.middleware.metamenth.interfaces.IPythonEntryPoint;
import com.middleware.metamenth.interfaces.datatypes.IBinaryMeasure;
import com.middleware.metamenth.interfaces.datatypes.IMeasure;
import com.middleware.metamenth.interfaces.structure.IRoom;

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
