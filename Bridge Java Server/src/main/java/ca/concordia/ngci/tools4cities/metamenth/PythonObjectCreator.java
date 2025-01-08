package ca.concordia.ngci.tools4cities.metamenth;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.ngci.tools4cities.metamenth.enums.BuildingType;
import ca.concordia.ngci.tools4cities.metamenth.enums.DataMeasurementType;
import ca.concordia.ngci.tools4cities.metamenth.enums.FloorType;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeasurementUnit;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterMeasureMode;
import ca.concordia.ngci.tools4cities.metamenth.enums.MeterType;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasure;
import ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasureType;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.IPythonEntryPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAddress;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IBinaryMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IMeter;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.ISensorData;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherData;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherStation;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IBuilding;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IFloor;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IRoom;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.transducers.ISensor;

/**
 * Uses create building objects by access the appropriate middleware
 * operations and producers, and MetamEnTh classes
 * @author Peter Yefi
 */

public class PythonObjectCreator {

    /**
     * Creates a building model for the LB Building. Must use data and operations from 
     * the middleware to create the building 
     * @param pythonEntryPoint, Python object with method to create MetamEnTh objects
     * @return
     */
    public IBuilding createLBBuilding(IPythonEntryPoint pythonEntryPoint){
       
        IMeasure roomSize  =  pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 20);
        IBinaryMeasure measurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(roomSize, "Binary");
        IRoom roomOne = pythonEntryPoint.createRoom(measurement, "Room 001", "Office", "hei.ies.ies");

        ISensor sensor = pythonEntryPoint.createSensor("TMP 01", SensorMeasure.TEMPERATURE.getValue(), MeasurementUnit.DEGREE_CELSIUS.getValue(), SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(), 900);
        
        ArrayList<ISensorData> sensorData = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            sensorData.add(pythonEntryPoint.createSensorData( index + 10, null));
            
        }
         List<Object> sensorDataObjs = new ArrayList<>(sensorData);
        sensor.addData(sensorDataObjs);

        roomOne.addTransducer(sensor);
        
        IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
        IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
        IFloor floorOne =  pythonEntryPoint.createFloor(measurement, 1, FloorType.REGULAR.getValue(), floorMeasurement, "First floor of the building", roomOne, null);
        
        IPoint coordinates =  pythonEntryPoint.createCoordinates(45.4967765, -73.5806159);
        IAddress address = pythonEntryPoint.createAddress("Montreal", "1400 de Maisonneuve Blvd. W.", "QC", "H3G 1M8", "Canada", coordinates);
        IMeasure buildingHeightMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.METERS.getValue(), 15);
        IBinaryMeasure buildingHeight = (IBinaryMeasure) pythonEntryPoint.createMeasurement(buildingHeightMeasure, "Binary");

        IMeasure floorAreaMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 50591.3);
        IBinaryMeasure floorArea = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorAreaMeasure, "Binary");
       
        IBuilding building = pythonEntryPoint.createBuilding(1996, buildingHeight, floorArea, address, BuildingType.NON_COMMERCIAL.getValue(), floorOne);
        IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        building.addMeter(meter);

        IWeatherStation weatherStation = pythonEntryPoint.createWeatherStation("LB WS");
        ArrayList<IWeatherData> weatherData = new ArrayList<>();

        for (int index = 0; index < 10; index++) {
            IMeasure weatherDataMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.RELATIVE_HUMIDITY.getValue(), index + 40);
            IBinaryMeasure relativeHumidity = (IBinaryMeasure) pythonEntryPoint.createMeasurement(weatherDataMeasure, "Binary");
            relativeHumidity.setMeasureType(DataMeasurementType.RELATIVE_HUMIDITY.getValue());
            weatherData.add(pythonEntryPoint.createWeatherData(relativeHumidity, null));
        }

        weatherStation.addWeatherData(weatherData);
        building.addWeatherStation(weatherStation);
        return building;
    }
}
