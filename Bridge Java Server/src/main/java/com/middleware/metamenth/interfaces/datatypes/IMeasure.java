package com.middleware.metamenth.interfaces.datatypes;


public interface IMeasure {
    String toString();
    String getUnit();
    void setUnit(String unit);
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
