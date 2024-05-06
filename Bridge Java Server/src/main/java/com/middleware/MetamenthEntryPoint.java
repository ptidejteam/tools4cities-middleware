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
        // GatewayServer.turnLoggingOff();
        // GatewayServer server = new GatewayServer();
        // server.start();
        // IFloor floor =  (IFloor) server.getPythonServerEntryPoint(new Class[] { IFloor.class });

        // try {
           
        //     System.out.print(floor);
        //     System.out.println(floor.getFloorType());
           
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // server.shutdown();
        GatewayServer gatewayServer = new GatewayServer(new MetamenthEntryPoint());
        gatewayServer.start();
  }
}
