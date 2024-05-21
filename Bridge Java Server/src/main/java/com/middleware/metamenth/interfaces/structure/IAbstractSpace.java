package com.middleware.metamenth.interfaces.structure;

import com.middleware.metamenth.interfaces.datatypes.IAbstractMeasure;

public interface IAbstractSpace {
    String toString();
    String getUID();
    IAbstractMeasure getArea();
    void setArea(IAbstractMeasure abstractMeasure);
    String getLocation();
    void setLocation(String location);
}
