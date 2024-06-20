package ca.concordia.ngci.tools4cities.metamenth.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.concordia.ngci.tools4cities.metamenth.enums.BuildingType;
import ca.concordia.ngci.tools4cities.metamenth.enums.DataMeasurementType;
import ca.concordia.ngci.tools4cities.metamenth.enums.FloorType;
import ca.concordia.ngci.tools4cities.metamenth.enums.HvacType;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeasurementUnit;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterAccumulationFrequency;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterMeasureMode;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterType;
import ca.concordia.ngci.tools4cities.metamenth.enums.OpenSpaceType;
import ca.concordia.ngci.tools4cities.metamenth.enums.RoomType;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorLogType;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasure;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasureType;
import ca.concordia.ngci.tools4cities.metamenth.enums.ZoneType;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.IPythonEntryPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAddress;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IBinaryMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IZone;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IMeter;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.ISensorData;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherData;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherStation;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IBuilding;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IFloor;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    			Thread.sleep(90);
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
    public void testCreateZone() {
    	IZone zone = pythonEntryPoint.createZone("HVAC Zone", ZoneType.HVAC.getValue());
    	zone.setDescription("HVAC zone in a building");
    	zone.setHvacType(HvacType.INTERIOR.getValue());
    	Assert.assertNotNull(zone.getUID());
    	Assert.assertEquals(zone.getZoneType(), ZoneType.HVAC.getValue());
    }
    
    @Test 
    public void testCreateAddress() {
    	IPoint coordinates =  pythonEntryPoint.createCoordinates(45.4967765, -73.5806159);
        IAddress address = pythonEntryPoint.createAddress("Montreal", "1400 de Maisonneuve Blvd. W.", "QC", "H3G 1M8", "Canada", coordinates);
        Assert.assertEquals(address.getState(), "QC");
        Assert.assertEquals(address.getCity(), "Montreal");
        Assert.assertEquals(address.getGeocoordinates().toString(), coordinates.toString());
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
    public void testCreateRoomWithZone() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        IZone zone = pythonEntryPoint.createZone("HVAC Zone", ZoneType.HVAC.getValue());
        room.addZone(zone);
        Assert.assertEquals(room.getZoneByName(zone.getName()).toString(), zone.toString());
        Assert.assertNull(room.getZoneByName(zone.getName()).getHvacType());
    }
    
    @Test 
    public void testCreateRoomInTwoZones() {
    	 IZone innerZone = pythonEntryPoint.createZone("HVAC Zone 1", ZoneType.HVAC.getValue());
    	 innerZone.setHvacType(HvacType.INTERIOR.getValue());
    	 IZone outerZone = pythonEntryPoint.createZone("HVAC Zone 2", ZoneType.HVAC.getValue());
    	 outerZone.setHvacType(HvacType.PERIMETER.getValue());
    	 
    	 IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
         IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
         IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
         room.addZone(outerZone);
         room.addZone(innerZone);
         
         Assert.assertEquals(room.getZones().size(), 2);
         Assert.assertEquals(room.getZoneByName(outerZone.getName()).toString(), outerZone.toString());   
    }
    
    @Test public void testRemoveZoneFromRoom() {
    	IZone innerZone = pythonEntryPoint.createZone("HVAC Zone 1", ZoneType.HVAC.getValue());
   	 	innerZone.setHvacType(HvacType.INTERIOR.getValue());
   	 	IZone outerZone = pythonEntryPoint.createZone("HVAC Zone 2", ZoneType.HVAC.getValue());
   	 	outerZone.setHvacType(HvacType.PERIMETER.getValue());
   	 
   	 	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        room.addZone(outerZone);
        room.addZone(innerZone);
        
        room.removeZone(outerZone);
        Assert.assertNull(room.getZoneByName(outerZone.getName()));
        Assert.assertEquals(room.getZones().size(), 1);
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
    public void testGetMeterMeasureByDate() {
        IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        LocalDate startDate = LocalDate.now().minusDays(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
   	 	for (int i = 0; i < 10; i++) {
   	 		LocalDate currentDate = startDate.plusDays(i);
   	 		meter.addMeterMeasure(pythonEntryPoint.createMeterMeasure(i+1, currentDate.format(formatter)));
   	 	}
   	 	
   	 	Assert.assertEquals(meter.getMeterMeasureByDate("2024-06-10", "2024-06-13").size(), 4);
   	 	Assert.assertEquals(meter.getMeterMeasureByDate("2024-06-10", null).size(), 10);
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
    public void testCreateRoomWithMeter() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        
        IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        meter.setMeterLocation("hei.ies.ies");
   	 	room.setMeter(meter);
   	 	Assert.assertEquals(room.getMeter().getMeasurementFrequency(), meter.getMeasurementFrequency(), 0.0001);
   	 	Assert.assertEquals(meter.toString(), room.getMeter().toString());
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
    public void testRemoveSensorFromRoom() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        ISensor sensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.HUMIDITY.getValue(), MeasurementUnit.RELATIVE_HUMIDITY.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        room.addTransducer(sensor);
        //remove sensor from room
        room.removeTransducer(sensor);
        Assert.assertNull(room.getTransducer(sensor.getName()));
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
    
    @Test
    public void testCreateFloorWithRoom() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
    	IBinaryMeasure roomMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(roomMeasurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
    	IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
        IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
        IFloor floor =  pythonEntryPoint.createFloor(floorMeasurement, 1, FloorType.REGULAR.getValue(), floorMeasurement, "First floor of the building", room, null);
        
       
        Map<String, Object> roomProperties = new HashMap<String, Object>();
        roomProperties.put("Name", room.getName());
        List<IRoom> floorRoom = floor.getRooms(roomProperties);
        
        Assert.assertEquals(floor.getRoomByName(room.getName()).toString(), room.toString());
        Assert.assertEquals(floorRoom.size(), 1);
        Assert.assertEquals(floorRoom.get(0).toString(), room.toString());
        Assert.assertEquals(floor.getArea().toString(), floorMeasurement.toString());
        Assert.assertEquals(floor.getFloorType(), FloorType.REGULAR.getValue());
    }
    
    @Test
    public void testCreateFloorWithRoomAndOpenSpace() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
    	IBinaryMeasure roomMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(roomMeasurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        
        IOpenSpace openSpace = pythonEntryPoint.createOpenSpace(roomMeasurement, "Hall 001", OpenSpaceType.CORRIDOR.getValue(), null);
        
        IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
        IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
        IFloor floor =  pythonEntryPoint.createFloor(floorMeasurement, 1, FloorType.REGULAR.getValue(), floorMeasurement, "First floor of the building", room, openSpace);
        
        Map<String, Object> openSpaceProperties = new HashMap<String, Object>();
        openSpaceProperties.put("Name", openSpace.getName());
        List<IOpenSpace> floorOpenSpace = floor.getOpenSpaces(openSpaceProperties);
        
        
        Assert.assertEquals(floor.getOpenSpaceById(openSpace.getUID()).toString(), openSpace.toString());
        Assert.assertEquals(floor.getRoomById(room.getUID()).toString(), room.toString());
        Assert.assertEquals(floorOpenSpace.size(), 1);
        Assert.assertEquals(floorOpenSpace.get(0).toString(), openSpace.toString());
    	
    }
    
    @Test public void testCreateFloorWithRoomHavingMeter() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.OFFICE.getValue(), "hei.ies.ies");
        
        IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        meter.setMeterLocation("hei.ies.ies");
   	 	room.setMeter(meter);
   	 	
   	 	IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
   	 	IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
   	 	IFloor floor =  pythonEntryPoint.createFloor(floorMeasurement, 1, FloorType.REGULAR.getValue(), floorMeasurement, "First floor of the building", room, null);
   	 	
   	 	Assert.assertEquals(floor.getRoomByName(room.getName()).getMeter().toString(), meter.toString());
    	Assert.assertEquals(floor.getRoomById(room.getUID()).toString(), room.toString());
    }
    
    @Test 
    public void testCreateFloorWithRoomHavingMeterAndSensor() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.KITCHEN.getValue(), "hei.ies.ies");
        

        IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        meter.setMeterLocation("hei.ies.ies");
        ISensor sensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        room.setMeter(meter);
        room.addTransducer(sensor);
        
        IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
   	 	IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
   	 	IFloor floor =  pythonEntryPoint.createFloor(floorMeasurement, 1, FloorType.BASEMENT.getValue(), floorMeasurement, "First floor of the building", room, null);
   	 	
   	 	Assert.assertEquals(floor.getRoomByName(room.getName()).getTransducer(sensor.getName()).toString(), sensor.toString());
   	 	Assert.assertEquals(floor.getRoomByName(room.getName()).getMeter().toString(), meter.toString());
   	 	Assert.assertEquals(floor.getFloorType(), FloorType.BASEMENT.getValue());
    	
    }
    
    @Test
    public void testCreateWeatherStation() {
    	IWeatherStation weatherStation = pythonEntryPoint.createWeatherStation("LB WS");
        ArrayList<IWeatherData> weatherData = new ArrayList<>();

        for (int index = 0; index < 10; index++) {
            IMeasure weatherDataMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.RELATIVE_HUMIDITY.getValue(), index + 40);
            IBinaryMeasure relativeHumidity = (IBinaryMeasure) pythonEntryPoint.createMeasurement(weatherDataMeasure, "Binary");
            relativeHumidity.setMeasureType(DataMeasurementType.RELATIVE_HUMIDITY.getValue());
            weatherData.add(pythonEntryPoint.createWeatherData(relativeHumidity, null));
        }
        
        weatherStation.addWeatherData(weatherData);
        
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        Assert.assertEquals(weatherStation.getWeatherData(null).size(), 10);
        Assert.assertEquals(weatherStation.getWeatherDataByDate(formattedDate, null).size(), 10);
        Assert.assertEquals(weatherStation.getWeatherData(null).get(0).getData().getMeasurementUnit(), MeasurementUnit.RELATIVE_HUMIDITY.getValue());
    }
    
    @Test 
    public void testCreateBuildingWithOneFloor() {
        IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.KITCHEN.getValue(), "hei.ies.ies");
        
        IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
   	 	IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
   	 	IFloor floor =  pythonEntryPoint.createFloor(floorMeasurement, 1, FloorType.BASEMENT.getValue(), floorMeasurement, "First floor of the building", room, null);

        IMeasure floorAreaMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 50591.3);
        IBinaryMeasure buildingFloorArea = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorAreaMeasure, "Binary");
        IPoint coordinates =  pythonEntryPoint.createCoordinates(45.4967765, -73.5806159);
        IAddress address = pythonEntryPoint.createAddress("Montreal", "1400 de Maisonneuve Blvd. W.", "QC", "H3G 1M8", "Canada", coordinates);
        
        IMeasure buildingHeightMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.METERS.getValue(), 15);
        IBinaryMeasure buildingHeight = (IBinaryMeasure) pythonEntryPoint.createMeasurement(buildingHeightMeasure, "Binary");
    	IBuilding building = pythonEntryPoint.createBuilding(1996, buildingHeight, buildingFloorArea, address, BuildingType.NON_COMMERCIAL.getValue(), floor);
        IMeter electricityMeter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        building.addMeter(electricityMeter);
        IMeter gasMeter = pythonEntryPoint.createMeter(90, MeasurementUnit.CUBIC_METER.getValue(), MeterType.GAS.getValue(), MeterMeasureMode.MANUAL.getValue());
        gasMeter.setDataAccumulated(true);
        gasMeter.setAccumulationFrequency(MeterAccumulationFrequency.WEEKLY.getValue());
        building.addMeter(gasMeter);
        
        
        Assert.assertEquals(building.getFloorById(floor.getUID()).toString(), floor.toString());
        Assert.assertEquals(building.getFloors(null).size(), 1);
        Assert.assertEquals(building.getAddress().toString(), address.toString());
        Assert.assertEquals(building.getFloorByNumber(1).getRooms(null).size(), 1);
        Assert.assertEquals(building.getMeters(null).size(), 2);
        Assert.assertEquals(building.getMeterByType(MeterType.GAS.getValue()).size(), 1);
        Assert.assertEquals(building.getFloorArea().toString(), buildingFloorArea.toString());
    }
    
    @Test
    public void testCreateBuildingWithFloors() {
    	IMeasure measure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(measure, "Binary");
        IRoom room = pythonEntryPoint.createRoom(measurement, "Room 001", RoomType.KITCHEN.getValue(), "hei.ies.ies");
        ISensor tempSensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        room.addTransducer(tempSensor);
         
        IMeasure osMeasure  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 49);
        IBinaryMeasure osMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(osMeasure, "Binary");
        IOpenSpace openSpace = pythonEntryPoint.createOpenSpace(osMeasurement, "Hall 001", OpenSpaceType.CORRIDOR.getValue(), null);
        ISensor co2Sensor = pythonEntryPoint.createSensor("CO2 01", SensorMeasure.CARBON_DIOXIDE.getValue(), MeasurementUnit.PARTS_PER_MILLION.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        openSpace.addTransducer(co2Sensor);
         
        IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
    	IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
    	IFloor floorOne =  pythonEntryPoint.createFloor(floorMeasurement, 1, FloorType.BASEMENT.getValue(), floorMeasurement, "First floor of the building", room, null);
    	 
    	IFloor floorTwo =  pythonEntryPoint.createFloor(floorMeasurement, 2, FloorType.REGULAR.getValue(), floorMeasurement, "Second floor of the building", null, openSpace);

        IMeasure floorAreaMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 50591.3);
        IBinaryMeasure buildingFloorArea = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorAreaMeasure, "Binary");
        IPoint coordinates =  pythonEntryPoint.createCoordinates(45.4967765, -73.5806159);
        IAddress address = pythonEntryPoint.createAddress("Montreal", "1400 de Maisonneuve Blvd. W.", "QC", "H3G 1M8", "Canada", coordinates);
         
        IMeasure buildingHeightMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.METERS.getValue(), 15);
        IBinaryMeasure buildingHeight = (IBinaryMeasure) pythonEntryPoint.createMeasurement(buildingHeightMeasure, "Binary");
     	IBuilding building = pythonEntryPoint.createBuilding(1996, buildingHeight, buildingFloorArea, address, BuildingType.NON_COMMERCIAL.getValue(), floorOne);
     	building.addFloor(floorTwo);
        IMeter electricityMeter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        building.addMeter(electricityMeter);
        IMeter gasMeter = pythonEntryPoint.createMeter(90, MeasurementUnit.CUBIC_METER.getValue(), MeterType.GAS.getValue(), MeterMeasureMode.MANUAL.getValue());
        gasMeter.setDataAccumulated(true);
        gasMeter.setAccumulationFrequency(MeterAccumulationFrequency.WEEKLY.getValue());
        building.addMeter(gasMeter);
        
        
        Map<String, Object> floorProperties = new HashMap<String, Object>();
        floorProperties.put("FloorType", FloorType.REGULAR.getValue());
        
        Assert.assertEquals(building.getFloors(null).size(), 2);
        Assert.assertEquals(building.getFloorByNumber(2).toString(), floorTwo.toString());
        Assert.assertEquals(building.getFloors(floorProperties).size(), 1);
        Assert.assertEquals(building.getFloorById(floorOne.getUID()).getOpenSpaces(null).size(), 0);
        Assert.assertEquals(building.getFloorByNumber(2).getRooms(null).size(), 0);
        Assert.assertEquals(building.getFloorById(floorOne.getUID()).getRooms(null).size(), 1);
        Assert.assertEquals(building.getFloorByNumber(2).getOpenSpaces(null).size(), 1);
        Assert.assertEquals(building.getFloorByNumber(1).getRoomByName(room.getName()).getTransducer(tempSensor.getName()).toString(), tempSensor.toString());
    }
}
