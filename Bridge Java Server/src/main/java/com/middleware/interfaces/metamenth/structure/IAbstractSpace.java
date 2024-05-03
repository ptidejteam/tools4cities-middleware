package com.middleware.interfaces.metamenth.structure;

import com.middleware.interfaces.metamenth.datatypes.IAbstractMeasure;

public interface IAbstractSpace {
    String toString();
    String getUID();
    IAbstractMeasure getArea();
    void setArea(IAbstractMeasure abstractMeasure);
    String getLocation();
    void setLocation(String location);
}
