package ca.concordia.ngci.tools4cities.metamenth.interfaces;

import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAbstractMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IAddress;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.datatypes.IZone;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IMeter;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IMeterMeasure;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.ISensorData;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherData;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.measureinstruments.IWeatherStation;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IBuilding;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IFloor;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IOpenSpace;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IRoom;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.transducers.ISensor;

/**
 * Interfaces to allow Java access the corresponding Python methods to create objects
 * from python classes
 * @author Peter Yefi
 */

public interface IPythonEntryPoint {
    IMeasure createMeasure(String unit, double minimum);
    IAbstractMeasure createMeasurement(IMeasure measure, String measureType);
    IRoom createRoom(IAbstractMeasure area, String name, String roomType, String location);
    IOpenSpace createOpenSpace(IAbstractMeasure area, String name, String spaceType, String location);
    IFloor createFloor(IAbstractMeasure area, Object floorNumber, String floorType, IAbstractMeasure height, String des, IRoom room);
    IMeter createMeter(double measurementFreq, String unit, String meterType, String measureMode);
    IMeterMeasure createMeterMeasure(double value, String timestamp);
    IPoint createCoordinates(double lat, double lon);
    IAddress createAddress(String city, String street, String state, String zipCode, String country, IPoint coordinates);
    IBuilding createBuilding(int constructionYear, IAbstractMeasure height, IAbstractMeasure floor_area, IAddress address, 
    String buildingType, IFloor floor);
    ISensor createSensor(String name, String measure, String unit, String measure_type, int data_frequency);
    ISensorData createSensorData(double value, String timestamp);
    IWeatherStation createWeatherStation(String name);
    IWeatherData createWeatherData(IAbstractMeasure data, String timestamp);
    IZone createZone(String name, String zone_type);
}
