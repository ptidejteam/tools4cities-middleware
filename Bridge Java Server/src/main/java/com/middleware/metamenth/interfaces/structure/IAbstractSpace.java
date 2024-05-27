package com.middleware.metamenth.interfaces.structure;

import com.middleware.metamenth.interfaces.datatypes.IAbstractMeasure;
import com.middleware.metamenth.interfaces.datatypes.IAbstractZonalEntity;

public interface IAbstractSpace extends IAbstractZonalEntity {
    String toString();
    String getUID();
    IAbstractMeasure getArea();
    void setArea(IAbstractMeasure abstractMeasure);
    String getLocation();
    void setLocation(String location);
}
