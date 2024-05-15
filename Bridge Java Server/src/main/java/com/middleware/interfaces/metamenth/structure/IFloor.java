package com.middleware.interfaces.metamenth.structure;

import com.middleware.interfaces.metamenth.datatypes.IAbstractMeasure;

public interface IFloor {
    String toString();
    String getDescription();
    void setDescription(String description);
    int getNumber();
    void setNumber(int number);
    String getFloorType();
    void setFloorType(String flootType);
    IAbstractMeasure getHeight();
    void setHeight(IAbstractMeasure height);
    void addOpenSpace(IOpenSpace openSpace);
    void addRoom(IRoom room);
}
