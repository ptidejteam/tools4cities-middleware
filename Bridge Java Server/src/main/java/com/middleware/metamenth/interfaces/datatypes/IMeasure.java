package com.middleware.metamenth.interfaces.datatypes;

import com.middleware.metamenth.enums.MeasurementUnit;

public interface IMeasure {
    String toString();
    MeasurementUnit getUnit();
    void setUnit(MeasurementUnit unit);
    float getMinimum();
    void setMinimum(float minimum);
    float getMaximum();
    void setMaximum(float maximum);
    float getSlope();
    void setSlope(float slope);
    float getMantissa();
    void setMantissa(float mantissa);
    float getExponent();
    void setExponent(float exponent);
}
