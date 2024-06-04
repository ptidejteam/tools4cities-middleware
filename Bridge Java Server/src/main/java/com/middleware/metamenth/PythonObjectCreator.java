package com.middleware.metamenth;

import com.middleware.metamenth.enums.BuildingType;
import com.middleware.metamenth.enums.FloorType;
import com.middleware.metamenth.enums.MeasurementUnit;
import com.middleware.metamenth.enums.MeterMeasureMode;
import com.middleware.metamenth.enums.MeterType;
import com.middleware.metamenth.interfaces.IPythonEntryPoint;
import com.middleware.metamenth.interfaces.datatypes.IAddress;
import com.middleware.metamenth.interfaces.datatypes.IBinaryMeasure;
import com.middleware.metamenth.interfaces.datatypes.IMeasure;
import com.middleware.metamenth.interfaces.datatypes.IPoint;
import com.middleware.metamenth.interfaces.measureinstruments.IMeter;
import com.middleware.metamenth.interfaces.structure.IBuilding;
import com.middleware.metamenth.interfaces.structure.IFloor;
import com.middleware.metamenth.interfaces.structure.IRoom;

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

        
        IMeasure floorSize  = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 150);
        IBinaryMeasure floorMeasurement = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorSize, "Binary");
        IFloor floorOne =  pythonEntryPoint.createFloor(measurement, 1, FloorType.REGULAR.getValue(), floorMeasurement, "First floor of the building", roomOne);
        
        IPoint coordinates =  pythonEntryPoint.createCoordinates(45.4967765, -73.5806159);
        IAddress address = pythonEntryPoint.createAddress("Montreal", "1400 de Maisonneuve Blvd. W.", "QC", "H3G 1M8", "Canada", coordinates);
        IMeasure buildingHeightMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.METERS.getValue(), 15);
        IBinaryMeasure buildingHeight = (IBinaryMeasure) pythonEntryPoint.createMeasurement(buildingHeightMeasure, "Binary");

        IMeasure floorAreaMeasure = pythonEntryPoint.createMeasure(MeasurementUnit.SQUARE_METERS.getValue(), 50591.3);
        IBinaryMeasure floorArea = (IBinaryMeasure) pythonEntryPoint.createMeasurement(floorAreaMeasure, "Binary");
       
        IBuilding building = pythonEntryPoint.createBuilding(1996, buildingHeight, floorArea, address, BuildingType.NON_COMMERCIAL.getValue(), floorOne);
        IMeter meter = pythonEntryPoint.createMeter(90, MeasurementUnit.KILOWATTS_PER_HOUR.getValue(), MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue());
        building.addMeter(meter);
        return building;
    }
}
