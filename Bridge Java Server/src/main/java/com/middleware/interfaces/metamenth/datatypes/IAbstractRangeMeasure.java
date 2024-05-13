package com.middleware.interfaces.metamenth.datatypes;

public interface IAbstractRangeMeasure extends IAbstractMeasure {
    void setMinimum(float minimum);
    float getMinimum();
    void setMaximum(float maximum);
    float getMaximum();
}
