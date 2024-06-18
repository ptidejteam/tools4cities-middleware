package ca.concordia.ngci.tools4cities.metamenth.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.concordia.ngci.tools4cities.metamenth.enums.MeasurementUnit;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterMeasureMode;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterType;
import ca.concordia.ngci.tools4cities.metamenth.enums.OpenSpaceType;
import ca.concordia.ngci.tools4cities.metamenth.enums.RoomType;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorLogType;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasure;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasureType;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.IPythonEntryPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IBinaryMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IMeter;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.ISensorData;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IOpenSpace;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IRoom;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.transducers.ISensor;
import py4j.GatewayServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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
    			Thread.sleep(80);
    		}catch(InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
   
    
    @Before
    public void setUp() throws IOException{
        
        Path scriptPath = Paths.get("..", "Bridge Python Client", "metamenth", "server.py");
        ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptPath.toAbsolutePath().normalize().toString());
       
        process = processBuilder.start();
        
        waitPortAvailability(PORT, 30);
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
        Assert.assertEquals(room.getArea().getMeasurementUnit(), measurement.getMeasurementUnit());
        Assert.assertNull(room.getMeter());
    }
    
    @Test
    public void testMeter() {
    	 IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
    	 Assert.assertEquals(meter.getMeterType(), MeterType.ELECTRICITY.getValue());
    	 Assert.assertNotNull(meter.getUID());
    	 Assert.assertNull(meter.getMeterLocation());
    	 Assert.assertNull(meter.getAccumulationFrequency());
    	 Assert.assertEquals(meter.getMeasurementFrequency(), 90.0, 0.0001);
    	 Assert.assertEquals(meter.getMeterMeasures(null).size(), 0);
    }
    
    @Test
    public void testMeterWithMeasurements() {
    	 IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
    	 for (int i = 0; i < 10; i++) {
    		 meter.addMeterMeasure(pythonEntryPoint.createMeterMeasure(i+1, null));
    	 }
    	 Assert.assertEquals(meter.getMeterMeasures(null).size(), 10);
    	 
    	 LocalDate currentDate = LocalDate.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         String formattedDate = currentDate.format(formatter);
         
         Assert.assertEquals(meter.getMeterMeasureByDate(formattedDate, formattedDate).size(), 10);
         Assert.assertEquals(meter.getMeterMeasureByDate(formattedDate, null).size(), 10);
         double lastMeasure = (double) meter.getMeterMeasures(null).get(9).getValue();
         Assert.assertEquals(lastMeasure, 10.0, 0.0001);
    }
    
    @Test
    public void testCreateSensor() {
    	ISensor sensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
    	Assert.assertEquals(sensor.getCurrentValue(), null);
    	Assert.assertEquals(sensor.getSensorLogType(), null);
    	Assert.assertEquals(sensor.getMeasure(), SensorMeasure.TEMPERATURE.getValue());
    	Assert.assertEquals(sensor.getUnit(), MeasurementUnit.DEGREE_CELSIUS.getValue());
    	Assert.assertEquals(sensor.getMeasureType(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue());
    }
    
    @Test
    public void testCreateSensorWithData() {
    	ISensor sensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
    	ArrayList<ISensorData> sensorData = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            sensorData.add(pythonEntryPoint.createSensorData( index + 10, null));
        }
        List<Object> sensorDataObjs = new ArrayList<>(sensorData);
        sensor.addData(sensorDataObjs);
        Assert.assertEquals(sensor.getData(null).size(), 10);
        ISensorData firstData = (ISensorData) sensor.getData(null).get(0);
        double firstValue = (double) firstData.getValue();
        Assert.assertNotNull(firstData.getUID());
        Assert.assertEquals(firstValue, 10.0, 0.0001);
    }
    
    @Test 
    public void testCreateRoomWithSensor() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        ISensor sensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
    	ArrayList<ISensorData> sensorData = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            sensorData.add(pythonEntryPoint.createSensorData( index + 10, null));
        }
        List<Object> sensorDataObjs = new ArrayList<>(sensorData);
        sensor.addData(sensorDataObjs);
        room.addTransducer(sensor);
        Assert.assertEquals(sensor.toString(), room.getTransducer(sensor.getName()).toString());
        Assert.assertEquals(sensor.getData(null), room.getTransducer(sensor.getName()).getData(null));
    	
    }
    
    @Test
    public void testCreateOpenSpace() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 49);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IOpenSpace openSpace = pythonEntryPoint.createOpenSpace(measurement, "Hall 001", OpenSpaceType.CORRIDOR.getValue(), null);
        Assert.assertEquals(openSpace.getSpaceType(), OpenSpaceType.CORRIDOR.getValue());
        Assert.assertEquals(openSpace.getLocation(), null);
        Assert.assertEquals(openSpace.getArea().toString(), measurement.toString());
    	
    }
    
    @Test 
    public void testCreateOpenSpaceWithSensors() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 49);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IOpenSpace openSpace = pythonEntryPoint.createOpenSpace(measurement, "Hall 001", OpenSpaceType.CORRIDOR.getValue(), null);
        
        ISensor tempSensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        String description = "Measures temperature";
        tempSensor.addMetaData("description", description);
        ISensor co2Sensor = pythonEntryPoint.createSensor("CO2 01", SensorMeasure.CARBON_DIOXIDE.getValue(), MeasurementUnit.PARTS_PER_MILLION.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        co2Sensor.setSensorLogType(SensorLogType.POLLING.getValue());
        
        openSpace.addTransducer(co2Sensor);
        openSpace.addTransducer(tempSensor);
        
        Assert.assertEquals(openSpace.getTransducer(tempSensor.getName()).toString(), tempSensor.toString());
        Assert.assertEquals(openSpace.getTransducer(tempSensor.getName()).getMetaData().get("description"), description);
        Assert.assertEquals(openSpace.getTransducer(co2Sensor.getName()).toString(), co2Sensor.toString());
        Assert.assertEquals(co2Sensor.getSensorLogType(), SensorLogType.POLLING.getValue());
    }
    
    @Test
    public void testCreateOpenSpaceWithDuplicateSensors() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 49);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IOpenSpace openSpace = pythonEntryPoint.createOpenSpace(measurement, "Hall 001", OpenSpaceType.CORRIDOR.getValue(), null);
        
        ISensor tempSensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        ISensor tempSensor2 = pythonEntryPoint.createSensor("TMP 01",  SensorMeasure.TEMPERATURE.getValue(),  MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_B.getValue(), 1200);
        openSpace.addTransducer(tempSensor2);
        openSpace.addTransducer(tempSensor); //shouldn't be added
        Assert.assertEquals(openSpace.getTransducer("TMP 01").toString(), tempSensor2.toString());
        Assert.assertNotEquals(openSpace.getTransducer("TMP 01").toString(), tempSensor.toString());
       
    }
}
