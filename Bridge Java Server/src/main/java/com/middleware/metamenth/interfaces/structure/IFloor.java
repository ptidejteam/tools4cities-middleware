package com.middleware.metamenth.interfaces.structure;

import com.middleware.metamenth.interfaces.datatypes.IAbstractMeasure;

public interface IFloor {
    String toString();
    String getDescription();
    void setDescription(String description);
    Object getNumber();
    void setNumber(double number);
    String getFloorType();
    void setFloorType(String flootType);
    IAbstractMeasure getHeight();
    void setHeight(IAbstractMeasure height);
    void addOpenSpace(IOpenSpace openSpace);
    void addRoom(IRoom room);
}
