package com.middleware;


import py4j.GatewayServer;

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
