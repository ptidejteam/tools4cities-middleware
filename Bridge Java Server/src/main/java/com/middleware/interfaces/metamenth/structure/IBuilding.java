package com.middleware.interfaces.metamenth.structure;

import com.middleware.interfaces.metamenth.datatypes.IAbstractMeasure;
import com.middleware.interfaces.metamenth.datatypes.IAddress;
import com.middleware.interfaces.metamenth.measureinstruments.IMeter;

public interface IBuilding {
    String getUID();
    String getConstructionYear();
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
    String toString();

}
