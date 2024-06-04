package com.middleware.metamenth.interfaces;

import com.middleware.metamenth.interfaces.datatypes.IAbstractMeasure;
import com.middleware.metamenth.interfaces.datatypes.IAddress;
import com.middleware.metamenth.interfaces.datatypes.IMeasure;
import com.middleware.metamenth.interfaces.datatypes.IPoint;
import com.middleware.metamenth.interfaces.measureinstruments.IMeter;
import com.middleware.metamenth.interfaces.measureinstruments.IMeterMeasure;
import com.middleware.metamenth.interfaces.structure.IOpenSpace;
import com.middleware.metamenth.interfaces.structure.IRoom;
import com.middleware.metamenth.interfaces.structure.IBuilding;
import com.middleware.metamenth.interfaces.structure.IFloor;

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
    IFloor createFloor(IAbstractMeasure area, float floorNumber, String floorType, IAbstractMeasure height, String des, IRoom room);
    IMeter createMeter(float measurementRreq, String unit, String meterType, String measureMode);
    IMeterMeasure createMeasureMeasure(float value);
    IPoint createCoordinates(double lat, double lon);
    IAddress createAddress(String city, String street, String state, String zipCode, String country, IPoint coordinates);
    IBuilding createBuilding(int constructionYear, IAbstractMeasure height, IAbstractMeasure floor_area, IAddress address, 
    String buildingType, IFloor floor);
}
