package ca.concordia.ngci.tools4cities.metamenth.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.concordia.ngci.tools4cities.metamenth.enums.MeasurementUnit;
import ca.concordia.ngci.tools4cities.metamenth.enums.RoomType;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.IPythonEntryPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IBinaryMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IRoom;
import py4j.GatewayServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TestPythonServer {

    private Process process = null;
    private GatewayServer gatewayServer;
    private IPythonEntryPoint pythonEntryPoint = null;
    private static int PORT = 25333;
    
    public void waitPortAvailability(int port, long timeoutMillis) {
    	long startTime = System.currentTimeMillis();
    	long endTime = startTime + timeoutMillis;
    	
    	while(System.currentTimeMillis() < endTime) {
    		try(ServerSocket serverSocket = new ServerSocket(port)){
        		serverSocket.setReuseAddress(true);
        	}catch(IOException e) {
        		
        	}
    		
    		try {
    			Thread.sleep(70);
    		}catch(InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
   
    
    @Before
    public void setUp() throws IOException{
        
        Path scriptPath = Paths.get("..", "Bridge Python Client", "metamenth", "server.py");
        // Code to set up the test environment
        ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptPath.toAbsolutePath().normalize().toString());
       
        process = processBuilder.start();
        
        waitPortAvailability(PORT, 20);
        //Set up connection to Python server
        gatewayServer = new GatewayServer();
        gatewayServer.start();
        pythonEntryPoint = (IPythonEntryPoint) gatewayServer.getPythonServerEntryPoint(new Class[]{ IPythonEntryPoint.class });
         
        
    }

    @After
    public void tearDown() {
    	if (gatewayServer != null) {
            gatewayServer.shutdown();
        }

        if (process != null) {
            process.destroy();
        }
    }

    @Test
    public void testCreateBinaryMeasure(){
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        Assert.assertEquals(measurement.getMeasurementUnit(), MeasurementUnit.SQUARE_METERS.getValue());
        Assert.assertEquals(measurement.getValue(), measure.getMinimum(), 0.0001);
    }
    
    @Test
    public void testCreateRoom() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        Assert.assertEquals(room.getName(), "Room 001");
        Assert.assertEquals(room.getRoomType(), RoomType.OFFICE.getValue());
    }
}
