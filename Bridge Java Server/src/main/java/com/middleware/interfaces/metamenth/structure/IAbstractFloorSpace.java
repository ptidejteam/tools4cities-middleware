package com.middleware.interfaces.metamenth;

public interface IAbstractFloorSpace extends IAbstractSpace {
    String toString();
    String getName();
    void setName(String name);
    IMeter getMeter();
    void setMeter(IMeter meter);
}
