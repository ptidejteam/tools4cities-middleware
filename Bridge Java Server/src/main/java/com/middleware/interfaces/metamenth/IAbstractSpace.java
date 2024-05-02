package com.middleware.interfaces.metamenth;

public interface IAbstractSpace {
    String toString();
    String getUID();
    IAbstractMeasure getArea();
    void setArea(IAbstractMeasure abstractMeasure);
    String getLocation();
    void setLocation(String location);
}
