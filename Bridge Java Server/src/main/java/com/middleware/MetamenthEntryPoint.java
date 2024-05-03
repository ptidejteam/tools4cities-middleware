package com.middleware;

import com.middleware.interfaces.metamenth.structure.IRoom;

import py4j.GatewayServer;

public class MetamenthEntryPoint {
    public static void main(String[] args) {
        GatewayServer.turnLoggingOff();
        GatewayServer server = new GatewayServer();
        server.start();
        IRoom room = (IRoom) server.getPythonServerEntryPoint(new Class[] { IRoom.class });
        try {
            System.out.println("Location: " + room.getLocation());
            System.out.println("Name: " + room.getName());
            System.out.println("Meter: " + room.getMeter());
            System.out.println("Area: " + room.getArea());
            System.out.println("Room: " + room.getRoomType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        server.shutdown();
  }
}
