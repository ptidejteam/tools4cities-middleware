package com.middleware.metamenth.interfaces.measureinstruments;

public interface IAbstractDataMeasure {
    String getUID();
    Object getValue();
    void setValue(Object value);
    String getMeasurementType();
    void setMeasurementType(String measurementType);
    String getTimeStamp();
    String toString();
}