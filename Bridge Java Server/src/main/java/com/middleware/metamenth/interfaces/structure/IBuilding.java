package com.middleware.metamenth.interfaces.structure;

import com.middleware.metamenth.interfaces.datatypes.IAbstractMeasure;
import com.middleware.metamenth.interfaces.datatypes.IAddress;
import com.middleware.metamenth.interfaces.measureinstruments.IMeter;
import com.middleware.metamenth.interfaces.measureinstruments.IWeatherStation;

public interface IBuilding {
    String getUID();
    int getConstructionYear();
    void setConstructionYear(int year);
    IAbstractMeasure getHeight();
    void setHeight(IAbstractMeasure height);
    IAbstractMeasure getFloorArea();
    void setFloorArea(IAbstractMeasure floorArea);
    IAbstractMeasure getInternalMass();
    void setInternalMass(IAbstractMeasure internalMass);
    IAddress getAddress();
    void setAddress(IAddress address);
    String getBuildingType();
    void setBuildingType(String buildingType);
    void addFloor(IFloor floor);
    void addMeter(IMeter meter);
    IBuilding addWeatherStation(IWeatherStation weatherStation);
    IWeatherStation getWeatherStation(String name);
    Boolean removeWeatherStation(IWeatherStation weatherStation);
    String toString();

}
