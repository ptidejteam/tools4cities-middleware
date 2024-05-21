package com.middleware.metamenth.interfaces.datatypes;

import com.middleware.metamenth.enums.MeasurementUnit;

public interface IAbstractMeasure {
    String toString();
    String getMeasurementUnit();
    void setMeasurementUnit(MeasurementUnit measurementUnit);
}
